package com.asimalimikial.aas2.content

import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BundledContentPackAssetsTest {
    private fun assetsRoot(): File {
        val candidates = listOf(
            File("src/main/assets"),
            File("app/src/main/assets")
        )
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
        assertEquals("0.2.0", pack.version)
        assertEquals(4, pack.skills.size)
        assertEquals(12, pack.activities.size)
        assertEquals(8, pack.activities.count { it.language == "en" })
        assertEquals(4, pack.activities.count { it.language == "ar" })
        assertTrue(pack.activities.filter { it.language == "en" }.groupBy { it.skillId }.values.all { it.size == 2 })
        assertTrue(pack.activities.single { it.activityId == "kg2-count-02-en" }.prompt.contains("★ ★ ★ ★"))
        assertTrue(pack.skills.all { it.sourceReferenceIds.isNotEmpty() })
    }

    @Test
    fun grade2FoundationAssetLoadsAndContainsBothLanguages() = runBlocking<Unit> {
        val pack = repository.load("aas2-grade2-foundation")

        assertEquals("GRADE_2", pack.learnerPath)
        assertEquals("0.2.0", pack.version)
        assertEquals(6, pack.skills.size)
        assertEquals(18, pack.activities.size)
        assertEquals(12, pack.activities.count { it.language == "en" })
        assertEquals(6, pack.activities.count { it.language == "ar" })
        assertTrue(pack.activities.filter { it.language == "en" }.groupBy { it.skillId }.values.all { it.size == 2 })
        val measureActivity = pack.activities.single { it.activityId == "g2-measure-02-en" }
        assertTrue(measureActivity.prompt.contains("120 cm"))
        assertEquals("a", measureActivity.correctOptionId)
        assertTrue(pack.skills.all { it.sourceReferenceIds.isNotEmpty() })
    }

    @Test
    fun everyReferencedOptionIllustrationExistsAndIsAValidPng() = runBlocking<Unit> {
        val packs = listOf(
            repository.load("aas2-kg2-foundation"),
            repository.load("aas2-grade2-foundation")
        )
        val imagePaths = packs
            .flatMap { it.activities }
            .flatMap { it.options }
            .mapNotNull { it.imageAssetPath }
            .distinct()

        assertEquals(11, imagePaths.size)
        val pngSignature = byteArrayOf(
            0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
        )
        imagePaths.forEach { imagePath ->
            assertTrue(imagePath.startsWith("option-art/"))
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

}
