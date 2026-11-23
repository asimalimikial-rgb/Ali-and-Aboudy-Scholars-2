package com.asimalimikial.aas2.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import java.io.File
import java.security.MessageDigest
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * One player per visible activity. Starting a different clip always releases the previous
 * player so prompt, option and feedback recordings cannot overlap.
 */
@Stable
class OfflineAudioController(private val context: Context) {
    var activeAssetPath by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isPlaying by mutableStateOf(false)
        private set
    var hasError by mutableStateOf(false)
        private set

    private var player: MediaPlayer? = null

    fun toggle(assetPath: String) {
        if (activeAssetPath == assetPath && (isLoading || isPlaying)) {
            stop()
        } else {
            play(assetPath)
        }
    }

    fun stop() {
        val current = player
        player = null
        if (current != null) {
            runCatching { if (current.isPlaying) current.stop() }
            runCatching { current.release() }
        }
        activeAssetPath = null
        isLoading = false
        isPlaying = false
        hasError = false
    }

    fun release() = stop()

    private fun cachedAudioFile(assetPath: String): File {
        val directory = File(context.cacheDir, "aas2-offline-audio").apply { mkdirs() }
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(assetPath.toByteArray(Charsets.UTF_8))
            .joinToString("") { byte -> "%02x".format(byte.toInt() and 0xff) }
        val target = File(directory, "$digest.ogg")
        if (!target.isFile || target.length() == 0L) {
            val temporary = File(directory, "$digest.tmp")
            context.assets.open(assetPath).use { input ->
                temporary.outputStream().use { output -> input.copyTo(output) }
            }
            if (temporary.length() == 0L) {
                temporary.delete()
                throw IllegalStateException("Bundled audio asset is empty: $assetPath")
            }
            if (!temporary.renameTo(target)) {
                temporary.copyTo(target, overwrite = true)
                temporary.delete()
            }
        }
        return target
    }

    private fun play(assetPath: String) {
        stop()
        activeAssetPath = assetPath
        isLoading = true
        hasError = false
        try {
            val created = MediaPlayer()
            created.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            created.setDataSource(cachedAudioFile(assetPath).absolutePath)
            created.setOnPreparedListener { prepared ->
                if (player === prepared) {
                    isLoading = false
                    isPlaying = true
                    prepared.start()
                } else {
                    prepared.release()
                }
            }
            created.setOnCompletionListener { completed ->
                completed.release()
                if (player === completed) {
                    player = null
                    activeAssetPath = null
                    isLoading = false
                    isPlaying = false
                }
            }
            created.setOnErrorListener { failed, _, _ ->
                failed.release()
                if (player === failed) {
                    player = null
                    isLoading = false
                    isPlaying = false
                    hasError = true
                }
                true
            }
            player = created
            created.prepareAsync()
        } catch (_: Exception) {
            player?.let { runCatching { it.release() } }
            player = null
            isLoading = false
            isPlaying = false
            hasError = true
        }
    }
}
