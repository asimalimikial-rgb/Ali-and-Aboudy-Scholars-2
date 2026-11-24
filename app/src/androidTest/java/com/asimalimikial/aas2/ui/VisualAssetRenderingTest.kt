package com.asimalimikial.aas2.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.caverock.androidsvg.SVG
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import android.graphics.BitmapFactory
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.serialization.decodeFromString

@RunWith(AndroidJUnit4::class)
class VisualAssetRenderingTest {
    @Test
    fun allSevenPersonalBookCoversDecodeFromOfflineAssets() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val json = context.assets.open("book-covers/manifest.json").bufferedReader().use { it.readText() }
        val manifest = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
            .decodeFromString<CoverManifestForTest>(json)
        assertEquals(7, manifest.covers.size)
        manifest.covers.forEach { cover ->
            val bitmap = context.assets.open(cover.assetPath).use { BitmapFactory.decodeStream(it) }
            assertTrue("Cover should decode: ${cover.assetPath}", bitmap != null && bitmap.width >= 400 && bitmap.height >= 400)
        }
    }

    @kotlinx.serialization.Serializable
    data class CoverManifestForTest(val covers: List<CoverForTest>)

    @kotlinx.serialization.Serializable
    data class CoverForTest(val assetPath: String)

    @Test
    fun generatedQuestionAndOptionSvgsRenderFromOfflineAssets() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        listOf("question-art", "option-art").forEach { directory ->
            val filename = context.assets.list(directory)
                .orEmpty()
                .firstOrNull { it.endsWith(".svg", ignoreCase = true) }
                ?: throw AssertionError("No SVG asset found in $directory")

            val picture = SVG.getFromAsset(context.assets, "$directory/$filename")
                .renderToPicture(384, 180)
            val bitmap = Bitmap.createBitmap(384, 180, Bitmap.Config.ARGB_8888)
            Canvas(bitmap).drawPicture(picture)

            var visiblePixels = 0
            for (y in 0 until bitmap.height step 4) {
                for (x in 0 until bitmap.width step 4) {
                    if (Color.alpha(bitmap.getPixel(x, y)) > 0) visiblePixels += 1
                }
            }
            assertTrue("SVG should render visible pixels from $directory", visiblePixels > 0)
        }
    }
}
