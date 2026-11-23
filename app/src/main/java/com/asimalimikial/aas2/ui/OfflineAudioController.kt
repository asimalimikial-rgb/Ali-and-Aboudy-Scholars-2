package com.asimalimikial.aas2.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
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
            context.assets.openFd(assetPath).use { descriptor ->
                created.setDataSource(
                    descriptor.fileDescriptor,
                    descriptor.startOffset,
                    descriptor.length
                )
            }
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
