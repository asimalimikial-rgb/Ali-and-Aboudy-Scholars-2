package com.asimalimikial.aas2.content

import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BundledContentPackAssetsTest {
    private fun assetsRoot(): File {
        val candidates = listOf(File("src/main/assets"), File("app/src/main/assets"))
        return candidates.firstOrNull { it.isDirectory }
            ?: throw AssertionError("Could not locate app/src/main/assets from the JVM test working directory")
    }

    private val repository by lazy {
        val root = assetsRoot()
        AssetContentPackRepository(
            source = ContentPackTextSource { assetPath ->
                File(root, assetPath).readText(Charsets.UTF_8)
            },
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun kg2FoundationAssetLoadsAndContainsBothLanguages() = runBlocking<Unit> {
        val pack = repository.load("aas2-kg2-foundation")
        assertEquals("KG2", pack.learnerPath)
        assertEquals("0.2.1", pack.version)
        assertEquals(4, pack.skills.size)
        assertEquals(12, pack.activities.size)
        assertEquals(8, pack.activities.count { it.language == "en" })
        assertEquals(4, pack.activities.count { it.language == "ar" })
        assertTrue(pack.activities.filter { it.language == "en" }.groupBy { it.skillId }.values.all { it.size == 2 })
        assertTrue(pack.activities.single { it.activityId == "kg2-count-02-en" }.prompt.contains("★ ★ ★ ★"))
        assertTrue(pack.skills.all { it.sourceReferenceIds.isNotEmpty() })
    }

    @Test
    fun grade2PackLoadsBookUnitLessonAndExpandedEnglishScienceContent() = runBlocking<Unit> {
        val pack = repository.load("aas2-grade2-foundation")
        assertEquals("GRADE_2", pack.learnerPath)
        assertEquals("0.3.1", pack.version)
        assertEquals(4, pack.schemaVersion)
        assertEquals(13, pack.skills.size)
        assertEquals(50, pack.activities.size)
        assertEquals(44, pack.activities.count { it.language == "en" })
        assertEquals(6, pack.activities.count { it.language == "ar" })
        assertEquals(1, pack.books.size)
        assertEquals("macmillan-science-level2", pack.books.single().bookId)
        assertEquals(1, pack.units.size)
        assertEquals(4, pack.lessons.size)
        val scienceActivities = pack.activities.filter { it.lessonId != null }
        assertEquals(32, scienceActivities.size)
        pack.lessons.forEach { lesson ->
            assertEquals(8, scienceActivities.count { it.lessonId == lesson.lessonId })
        }
        assertTrue(scienceActivities.all { it.assessmentPhase in setOf("DIAGNOSTIC", "GUIDED", "INDEPENDENT", "TRANSFER") })
        assertTrue(scienceActivities.all { it.optionRationales.size == 3 })
        assertTrue(pack.skills.all { it.sourceReferenceIds.isNotEmpty() })
        val measureActivity = pack.activities.single { it.activityId == "g2-measure-02-en" }
        assertTrue(measureActivity.prompt.contains("120 cm"))
        assertEquals("a", measureActivity.correctOptionId)
    }

    @Test
    fun everyReferencedOptionIllustrationExistsAndIsAValidPng() = runBlocking<Unit> {
        val packs = listOf(
            repository.load("aas2-kg2-foundation"),
            repository.load("aas2-grade2-foundation")
        )
        val imagePaths = packs.flatMap { it.activities }
            .flatMap { it.options }
            .mapNotNull { it.imageAssetPath }
            .distinct()
        assertEquals(11, imagePaths.size)
        assertPngAssets(imagePaths, "option-art/")
    }

    @Test
    fun everyScienceQuestionHasItsOwnBundledIllustration() = runBlocking<Unit> {
        val pack = repository.load("aas2-grade2-foundation")
        val scienceActivities = pack.activities.filter { it.lessonId != null }
        val imagePaths = scienceActivities.mapNotNull { it.promptImageAssetPath }
        assertEquals(32, imagePaths.size)
        assertEquals(32, imagePaths.distinct().size)
        assertPngAssets(imagePaths, "question-art/")
    }

    private fun assertPngAssets(paths: List<String>, requiredPrefix: String) {
        val pngSignature = byteArrayOf(
            0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
        )
        paths.forEach { imagePath ->
            assertTrue("Unexpected asset path: $imagePath", imagePath.startsWith(requiredPrefix))
            val imageFile = File(assetsRoot(), imagePath)
            assertTrue("Missing bundled image: $imagePath", imageFile.isFile)
            val bytes = imageFile.readBytes()
            assertTrue("Image is too small: $imagePath", bytes.size > 1024)
            assertTrue(
                "Invalid PNG signature: $imagePath",
                bytes.take(8).toByteArray().contentEquals(pngSignature)
            )
        }
    }

    @Test
    fun EnglishPostAnswerFeedbackIsEncouragementOnly() = runBlocking<Unit> {
        val packs = listOf(
            repository.load("aas2-kg2-foundation"),
            repository.load("aas2-grade2-foundation")
        )
        val englishActivities = packs.flatMap { it.activities }.filter { it.language == "en" }
        assertTrue(englishActivities.isNotEmpty())
        englishActivities.forEach { activity ->
            assertTrue("Correct feedback should be brief: ${activity.activityId}", activity.feedbackCorrect.length <= 32)
            assertTrue("Incorrect feedback should be brief encouragement: ${activity.activityId}", activity.feedbackIncorrect.length <= 48)
            assertTrue("Incorrect feedback must not explain the answer: ${activity.activityId}",
                listOf("because", "roots", "leaves", "tens place", "compare", "add the").none {
                    activity.feedbackIncorrect.contains(it, ignoreCase = true)
                }
            )
        }
    }

    @Test
    fun everyEnglishActivityHasUniqueBundledOfflineAudioForPromptOptionsAndFeedback() = runBlocking<Unit> {
        val packs = listOf(
            repository.load("aas2-kg2-foundation"),
            repository.load("aas2-grade2-foundation")
        )
        val englishActivities = packs.flatMap { it.activities }.filter { it.language == "en" }
        assertEquals(52, englishActivities.size)
        val allPaths = mutableListOf<String>()
        englishActivities.forEach { activity ->
            val paths = listOfNotNull(
                activity.promptAudioAssetPath,
                activity.feedbackCorrectAudioAssetPath,
                activity.feedbackIncorrectAudioAssetPath
            ) + activity.optionAudioAssetPaths.values
            assertEquals("Every answer option must have audio for ${activity.activityId}", activity.options.size + 3, paths.size)
            assertTrue(paths.all { it.startsWith("speech/") })
            allPaths += paths
        }
        assertEquals("Every generated clip is tied to one question role", allPaths.size, allPaths.distinct().size)
        allPaths.forEach { audioPath ->
            val file = File(assetsRoot(), audioPath)
            assertTrue("Missing offline audio: $audioPath", file.isFile)
            val bytes = file.readBytes()
            assertTrue("Audio file is too small: $audioPath", bytes.size > 100)
            assertTrue(
                "Invalid OGG container: $audioPath",
                bytes.take(4).toByteArray().contentEquals("OggS".toByteArray(Charsets.US_ASCII))
            )
        }
    }

}
