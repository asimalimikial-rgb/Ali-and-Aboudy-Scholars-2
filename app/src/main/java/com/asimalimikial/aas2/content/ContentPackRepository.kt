package com.asimalimikial.aas2.content

import java.io.FileNotFoundException
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun interface ContentPackTextSource {
    fun readText(assetPath: String): String
}

interface ContentPackRepository {
    suspend fun load(packId: String): ContentPack
}

class InvalidContentPackIdException(packId: String) :
    IllegalArgumentException("Unsafe content pack ID: $packId")

class ContentPackNotFoundException(
    packId: String,
    cause: Throwable? = null
) : Exception("Content pack '$packId' was not found in app assets", cause)

class ContentPackReadException(
    packId: String,
    cause: Throwable
) : Exception("Content pack '$packId' could not be read from app assets", cause)

class ContentPackFormatException(
    packId: String,
    cause: Throwable
) : Exception("Content pack '$packId' contains invalid JSON", cause)

class InvalidContentPackDefinitionException(
    val validationErrors: List<String>
) : Exception("Content pack failed validation")

/**
 * Loads bundled JSON content packs without network access.
 * Pack IDs are constrained before being used in an asset path, and the decoded
 * pack must identify itself with the requested ID and pass the current validator.
 */
class AssetContentPackRepository(
    private val source: ContentPackTextSource,
    private val json: Json = Json,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ContentPackRepository {
    override suspend fun load(packId: String): ContentPack {
        if (!SAFE_PACK_ID.matches(packId)) {
            throw InvalidContentPackIdException(packId)
        }

        return withContext(ioDispatcher) {
            val assetPath = "content-packs/$packId.json"
            val rawJson = try {
                source.readText(assetPath)
            } catch (notFound: FileNotFoundException) {
                throw ContentPackNotFoundException(packId, notFound)
            } catch (readFailure: IOException) {
                throw ContentPackReadException(packId, readFailure)
            }

            val pack = try {
                json.decodeFromString<ContentPack>(rawJson)
            } catch (invalidJson: SerializationException) {
                throw ContentPackFormatException(packId, invalidJson)
            }

            if (pack.packId != packId) {
                throw InvalidContentPackDefinitionException(
                    listOf("Asset ID '$packId' does not match declared pack ID '" + pack.packId + "'")
                )
            }

            val validation = ContentPackValidator.validate(pack)
            if (!validation.isValid) {
                throw InvalidContentPackDefinitionException(validation.errors)
            }

            pack
        }
    }

    private companion object {
        val SAFE_PACK_ID = Regex("[a-z0-9][a-z0-9._-]{0,63}")
    }
}
