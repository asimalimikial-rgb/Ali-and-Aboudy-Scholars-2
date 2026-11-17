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
        assertEquals(4, pack.skills.size)
        assertEquals(8, pack.activities.size)
        assertTrue(pack.activities.any { it.language == "en" })
        assertTrue(pack.activities.any { it.language == "ar" })
        assertTrue(pack.skills.all { it.sourceReferenceIds.isNotEmpty() })
    }

    @Test
    fun grade2FoundationAssetLoadsAndContainsBothLanguages() = runBlocking<Unit> {
        val pack = repository.load("aas2-grade2-foundation")

        assertEquals("GRADE_2", pack.learnerPath)
        assertEquals(6, pack.skills.size)
        assertEquals(12, pack.activities.size)
        assertTrue(pack.activities.any { it.language == "en" })
        assertTrue(pack.activities.any { it.language == "ar" })
        assertTrue(pack.skills.all { it.sourceReferenceIds.isNotEmpty() })
    }
}
