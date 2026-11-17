package com.asimalimikial.aas2.content

import java.io.FileNotFoundException
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AssetContentPackRepositoryTest {
    private fun validPack(packId: String = "ali-numeracy-foundation") = ContentPack(
        packId = packId,
        version = "0.1.0",
        schemaVersion = 3,
        learnerPath = "KG2",
        skills = listOf(
            SkillDefinition(
                skillId = "counting",
                learnerPath = "KG2",
                domain = "Numeracy",
                observableOutcome = "Counts a small set of objects"
            )
        )
    )

    @Test
    fun loadsOnlyTheRequestedBundledPackAndValidatesIt() = runBlocking<Unit> {
        val expected = validPack()
        var requestedPath: String? = null
        val source = ContentPackTextSource { path ->
            requestedPath = path
            Json.encodeToString(expected)
        }
        val repository = AssetContentPackRepository(source, ioDispatcher = Dispatchers.Unconfined)

        val loaded = repository.load(expected.packId)

        assertEquals("content-packs/ali-numeracy-foundation.json", requestedPath)
        assertEquals(expected, loaded)
    }

    @Test
    fun rejectsUnsafePackIdBeforeReadingAnyAsset() = runBlocking<Unit> {
        var wasRead = false
        val repository = AssetContentPackRepository(
            source = ContentPackTextSource {
                wasRead = true
                Json.encodeToString(validPack())
            },
            ioDispatcher = Dispatchers.Unconfined
        )

        try {
            repository.load("../private")
            throw AssertionError("Expected an unsafe pack ID to be rejected")
        } catch (_: InvalidContentPackIdException) {
            assertFalse(wasRead)
        }
    }

    @Test
    fun rejectsAssetWhenDeclaredPackIdDoesNotMatchRequestedId() = runBlocking<Unit> {
        val repository = AssetContentPackRepository(
            source = ContentPackTextSource { Json.encodeToString(validPack("different-pack")) },
            ioDispatcher = Dispatchers.Unconfined
        )

        try {
            repository.load("requested-pack")
            throw AssertionError("Expected mismatched pack ID to be rejected")
        } catch (invalid: InvalidContentPackDefinitionException) {
            assertTrue(invalid.validationErrors.single().contains("does not match"))
        }
    }

    @Test
    fun rejectsPackThatFailsContentValidation() = runBlocking<Unit> {
        val invalid = validPack().copy(schemaVersion = 99)
        val repository = AssetContentPackRepository(
            source = ContentPackTextSource { Json.encodeToString(invalid) },
            ioDispatcher = Dispatchers.Unconfined
        )

        try {
            repository.load(invalid.packId)
            throw AssertionError("Expected invalid content pack to be rejected")
        } catch (failure: InvalidContentPackDefinitionException) {
            assertTrue(failure.validationErrors.any { it.contains("Unsupported schemaVersion") })
        }
    }

    @Test
    fun malformedJsonProducesFormatException() = runBlocking<Unit> {
        val repository = AssetContentPackRepository(
            source = ContentPackTextSource { "{" },
            ioDispatcher = Dispatchers.Unconfined
        )

        try {
            repository.load("valid-pack")
            throw AssertionError("Expected malformed JSON to be rejected")
        } catch (_: ContentPackFormatException) {
            // Expected: parsing fails before a ContentPack can be validated.
        }
    }

    @Test
    fun missingAssetProducesNotFoundException() = runBlocking<Unit> {
        val repository = AssetContentPackRepository(
            source = ContentPackTextSource { throw FileNotFoundException(it) },
            ioDispatcher = Dispatchers.Unconfined
        )

        try {
            repository.load("missing-pack")
            throw AssertionError("Expected a missing asset to be reported")
        } catch (_: ContentPackNotFoundException) {
            // Expected: no fallback pack or network request is attempted.
        }
    }

    @Test
    fun unreadableAssetProducesReadException() = runBlocking<Unit> {
        val repository = AssetContentPackRepository(
            source = ContentPackTextSource { throw IOException("read failed") },
            ioDispatcher = Dispatchers.Unconfined
        )

        try {
            repository.load("unreadable-pack")
            throw AssertionError("Expected an asset read failure to be reported")
        } catch (_: ContentPackReadException) {
            // Expected: IO failures are not misreported as validation failures.
        }
    }
}
