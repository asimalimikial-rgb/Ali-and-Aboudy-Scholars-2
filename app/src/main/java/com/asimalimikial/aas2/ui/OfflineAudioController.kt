package com.asimalimikial.aas2.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import java.security.MessageDigest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** One lifecycle-bound player for bundled, offline learning audio. */
@Stable
class OfflineAudioController(context: Context) {
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var playJob: Job? = null
    private var player: MediaPlayer? = null

    var activeAssetPath by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isPlaying by mutableStateOf(false)
        private set
    var hasError by mutableStateOf(false)
        private set
    var failedAssetPath by mutableStateOf<String?>(null)
        private set

    fun toggle(assetPath: String) {
        if (activeAssetPath == assetPath && (isLoading || isPlaying)) stop() else play(assetPath)
    }

    fun play(assetPath: String) {
        playJob?.cancel()
        stopInternal()
        hasError = false
        failedAssetPath = null
        isLoading = true
        isPlaying = false
        activeAssetPath = assetPath

        playJob = scope.launch {
            var created: MediaPlayer? = null
            try {
                val cachedFile = withContext(Dispatchers.IO) {
                    trimAudioCacheIfNeeded(maxFiles = 60)
                    cachedAudioFile(assetPath)
                }
                val mediaPlayer = MediaPlayer()
                created = mediaPlayer
                player = mediaPlayer
                mediaPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                mediaPlayer.setOnPreparedListener { prepared ->
                    if (player === prepared && activeAssetPath == assetPath) {
                        isLoading = false
                        isPlaying = true
                        prepared.start()
                    } else {
                        runCatching { prepared.release() }
                    }
                }
                mediaPlayer.setOnCompletionListener { completed ->
                    if (player === completed) {
                        player = null
                        activeAssetPath = null
                        isLoading = false
                        isPlaying = false
                    }
                    runCatching { completed.release() }
                }
                mediaPlayer.setOnErrorListener { failed, _, _ ->
                    if (player === failed) {
                        player = null
                        activeAssetPath = null
                        isLoading = false
                        isPlaying = false
                        hasError = true
                        failedAssetPath = assetPath
                    }
                    runCatching { failed.release() }
                    true
                }
                mediaPlayer.setDataSource(cachedFile.absolutePath)
                mediaPlayer.prepareAsync()
            } catch (cancelled: CancellationException) {
                created?.let { runCatching { it.release() } }
                if (player === created) player = null
                throw cancelled
            } catch (_: Exception) {
                created?.let { runCatching { it.release() } }
                if (player === created) player = null
                isLoading = false
                isPlaying = false
                hasError = true
                failedAssetPath = assetPath
                activeAssetPath = null
            }
        }
    }

    fun stop() {
        playJob?.cancel()
        playJob = null
        stopInternal()
        isLoading = false
        isPlaying = false
        activeAssetPath = null
        hasError = false
        failedAssetPath = null
    }

    fun release() {
        playJob?.cancel()
        playJob = null
        scope.cancel()
        stopInternal()
        isLoading = false
        isPlaying = false
        activeAssetPath = null
    }

    private fun stopInternal() {
        val current = player
        player = null
        if (current != null) {
            runCatching { if (current.isPlaying) current.stop() }
            runCatching { current.release() }
        }
    }

    private fun cachedAudioFile(assetPath: String): File {
        val directory = File(appContext.cacheDir, "aas2-offline-audio").apply { mkdirs() }
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(assetPath.toByteArray(Charsets.UTF_8))
            .joinToString("") { byte -> "%02x".format(byte.toInt() and 0xff) }
        val target = File(directory, "$digest.ogg")
        if (target.isFile && target.length() > 0L) {
            target.setLastModified(System.currentTimeMillis())
            return target
        }
        val temporary = File(directory, "$digest.tmp")
        appContext.assets.open(assetPath).use { input ->
            temporary.outputStream().use { output -> input.copyTo(output) }
        }
        check(temporary.isFile && temporary.length() > 0L) { "Bundled audio asset is empty: $assetPath" }
        if (!temporary.renameTo(target)) {
            temporary.copyTo(target, overwrite = true)
            temporary.delete()
        }
        return target
    }

    private fun trimAudioCacheIfNeeded(maxFiles: Int) {
        val directory = File(appContext.cacheDir, "aas2-offline-audio")
        val files = directory.listFiles()?.filter { it.isFile && it.name.endsWith(".ogg") } ?: return
        if (files.size <= maxFiles) return
        files.sortedBy { it.lastModified() }.take(files.size - maxFiles).forEach { it.delete() }
    }
}
