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
        assertEquals("0.2.6", pack.version)
        assertEquals(23, pack.skills.size)
        assertEquals(136, pack.activities.size)
        assertEquals(132, pack.activities.count { it.language == "en" })
        assertEquals(4, pack.activities.count { it.language == "ar" })
        val originalEnglish = pack.activities.filter { it.language == "en" && !it.activityId.startsWith("new-") && !it.activityId.startsWith("aas2-kg2-") }
        assertEquals(8, originalEnglish.size)
        assertTrue(originalEnglish.groupBy { it.skillId }.values.all { it.size == 2 })
        assertEquals(100, pack.activities.count { it.activityId.startsWith("new-ali-") })
        assertTrue(pack.activities.single { it.activityId == "kg2-count-02-en" }.prompt.contains("★ ★ ★ ★"))
        assertTrue(pack.skills.all { it.sourceReferenceIds.isNotEmpty() })
        assertEquals(3, pack.books.size)
        assertEquals(10, pack.units.size)
        assertEquals(19, pack.lessons.size)
        assertTrue(pack.lessons.all { it.title.isNotBlank() && it.skillIds.isNotEmpty() })
        assertTrue(ContentPackValidator.validate(pack).errors.joinToString(), ContentPackValidator.validate(pack).isValid)
    }

    @Test
    fun grade2PackLoadsBookUnitLessonAndExpandedEnglishScienceContent() = runBlocking<Unit> {
        val pack = repository.load("aas2-grade2-foundation")
        assertEquals("GRADE_2", pack.learnerPath)
        assertEquals("0.4.8", pack.version)
        assertEquals(4, pack.schemaVersion)
        assertEquals(94, pack.skills.size)
        assertEquals(367, pack.activities.size)
        assertEquals(361, pack.activities.count { it.language == "en" })
        assertEquals(6, pack.activities.count { it.language == "ar" })
        assertEquals(100, pack.activities.count { it.activityId.startsWith("new-aboudy-") })
        assertEquals(5, pack.books.size)
        assertTrue(pack.books.any { it.bookId == "macmillan-science-level2" })
        assertEquals(46, pack.units.size)
        assertEquals(76, pack.lessons.size)
        assertEquals(12, pack.units.count { it.bookId == "english-world-level-2" })
        assertEquals(12, pack.units.count { it.bookId == "macmillan-mathematics-level-2a" })
        assertEquals(12, pack.units.count { it.bookId == "macmillan-mathematics-level-2b" })
        assertEquals(5, pack.units.count { it.bookId == "maths-xpress-book-2" })
        assertEquals(5, pack.units.count { it.bookId == "macmillan-science-level2" })
        assertTrue(ContentPackValidator.validate(pack).errors.joinToString(), ContentPackValidator.validate(pack).isValid)
        val scienceActivities = pack.activities.filter { it.lessonId != null }
        assertEquals(134, scienceActivities.size)
        val lessonSizes = scienceActivities.groupBy { it.lessonId }.values.map { it.size }
        assertEquals(9, lessonSizes.count { it == 8 })
        assertEquals(5, lessonSizes.count { it == 6 })
        assertTrue(scienceActivities.all { it.assessmentPhase in setOf("DIAGNOSTIC", "GUIDED", "INDEPENDENT", "TRANSFER") })
        assertTrue(scienceActivities.all { it.optionRationales.size == 3 })
        assertTrue(pack.skills.all { it.sourceReferenceIds.isNotEmpty() })
        val measureActivity = pack.activities.single { it.activityId == "g2-measure-02-en" }
        assertTrue(measureActivity.prompt.contains("120 cm"))
        assertEquals("a", measureActivity.correctOptionId)
    }

    @Test
    fun exactlyOneHundredNewActivitiesWereAddedForEachLearnerWithoutDuplicatePrompts() = runBlocking<Unit> {
        val packs = listOf(
            repository.load("aas2-kg2-foundation"),
            repository.load("aas2-grade2-foundation")
        )
        val newActivities = packs.flatMap { it.activities }.filter {
            it.activityId.startsWith("new-ali-") || it.activityId.startsWith("new-aboudy-")
        }
        assertEquals(200, newActivities.size)
        assertEquals(100, newActivities.count { it.activityId.startsWith("new-ali-") })
        assertEquals(100, newActivities.count { it.activityId.startsWith("new-aboudy-") })
        listOf("new-ali-", "new-aboudy-").forEach { prefix ->
            val learnerItems = newActivities.filter { it.activityId.startsWith(prefix) }
            val correctOptionCounts = listOf("a", "b", "c").map { optionId ->
                learnerItems.count { it.correctOptionId == optionId }
            }
            assertEquals(100, correctOptionCounts.sum())
            assertTrue(
                "Correct-option distribution should remain balanced for $prefix: $correctOptionCounts",
                correctOptionCounts.maxOrNull()!! - correctOptionCounts.minOrNull()!! <= 3
            )
        }
        assertEquals(200, newActivities.map { it.activityId }.distinct().size)
        assertEquals(200, newActivities.map { it.prompt.trim().lowercase() }.distinct().size)
        newActivities.forEach { activity ->
            assertEquals(3, activity.options.size)
            assertEquals(3, activity.options.map { it.text.trim() }.distinct().size)
            assertEquals(1, activity.options.count { it.optionId == activity.correctOptionId })
            assertTrue(activity.promptImageAssetPath?.startsWith("question-art/") == true)
        }
    }
    @Test
    fun everyReferencedOptionIllustrationExistsAsAnOfflineSvg() = runBlocking<Unit> {
        val packs = listOf(
            repository.load("aas2-kg2-foundation"),
            repository.load("aas2-grade2-foundation")
        )
        val imagePaths = packs.flatMap { it.activities }
            .flatMap { it.options }
            .mapNotNull { it.imageAssetPath }
            .distinct()
        assertTrue(imagePaths.isNotEmpty())
        assertTrue(imagePaths.all { it.endsWith(".svg", ignoreCase = true) })
        assertVisualAssets(imagePaths, "option-art/")
    }

    @Test
    fun everyEnglishQuestionHasItsOwnBundledIllustration() = runBlocking<Unit> {
        val packs = listOf(
            repository.load("aas2-kg2-foundation"),
            repository.load("aas2-grade2-foundation")
        )
        val englishActivities = packs.flatMap { it.activities }.filter { it.language == "en" }
        val imagePaths = englishActivities.mapNotNull { it.promptImageAssetPath }
        assertEquals(493, imagePaths.size)
        assertEquals("Each English question must have a unique image path", 493, imagePaths.distinct().size)
        assertTrue("Question art should contain SVG assets", imagePaths.any { it.endsWith(".svg", ignoreCase = true) })
        assertTrue("Text-heavy question art should retain PNG assets", imagePaths.any { it.endsWith(".png", ignoreCase = true) })
        assertVisualAssets(imagePaths, "question-art/")
    }

    private fun assertVisualAssets(paths: List<String>, requiredPrefix: String) {
        val pngSignature = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
        paths.forEach { imagePath ->
            assertTrue("Unexpected asset path: $imagePath", imagePath.startsWith(requiredPrefix))
            val imageFile = File(assetsRoot(), imagePath)
            assertTrue("Missing bundled visual: $imagePath", imageFile.isFile)
            val bytes = imageFile.readBytes()
            assertTrue("Visual asset is too small: $imagePath", bytes.size > 100)
            when {
                imagePath.endsWith(".png", ignoreCase = true) -> assertTrue(
                    "Invalid PNG signature: $imagePath",
                    bytes.take(8).toByteArray().contentEquals(pngSignature)
                )
                imagePath.endsWith(".svg", ignoreCase = true) -> {
                    val svgText = String(bytes, Charsets.UTF_8).trimStart()
                    assertTrue("Invalid SVG root: $imagePath", svgText.startsWith("<svg"))
                    assertTrue("Incomplete SVG document: $imagePath", svgText.contains("</svg>"))
                    assertTrue(
                        "SVG must not reference external files: $imagePath",
                        !Regex("""(?:href|src)\s*=\s*["'](?:https?:|file:)""", RegexOption.IGNORE_CASE)
                            .containsMatchIn(svgText)
                    )
                }
                else -> throw AssertionError("Unsupported visual format: $imagePath")
            }
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
        assertEquals(493, englishActivities.size)
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
