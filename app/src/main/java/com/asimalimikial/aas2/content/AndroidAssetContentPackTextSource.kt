package com.asimalimikial.aas2.content

import android.content.Context

/** Reads bundled content-pack JSON from the application's assets directory. */
class AndroidAssetContentPackTextSource(context: Context) : ContentPackTextSource {
    private val assets = context.applicationContext.assets

    override fun readText(assetPath: String): String =
        assets.open(assetPath).bufferedReader(Charsets.UTF_8).use { it.readText() }
}
