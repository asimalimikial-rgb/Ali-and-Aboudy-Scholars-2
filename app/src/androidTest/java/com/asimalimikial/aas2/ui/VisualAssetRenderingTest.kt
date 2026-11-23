package com.asimalimikial.aas2.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.caverock.androidsvg.SVG
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VisualAssetRenderingTest {
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
