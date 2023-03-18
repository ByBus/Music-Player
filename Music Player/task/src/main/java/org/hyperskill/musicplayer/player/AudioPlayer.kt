package org.hyperskill.musicplayer.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import org.hyperskill.musicplayer.domain.PlaybackState
import org.hyperskill.musicplayer.domain.Player
import org.hyperskill.musicplayer.domain.Song
import org.hyperskill.musicplayer.ui.Communication
import java.io.Closeable

class AudioPlayer(
    private val appContext: Context,
    private val playbackState: Communication.Update<PlaybackState>,
    private var progressWatcher: Worker
) : Player, MediaPlayerCombinedListener, Closeable {
    private var mediaPlayer: MediaPlayer? = null
    private var playAfterPrepare: Boolean = true
    private var duration: Long = 0L
    private val position: Long
        get() = mediaPlayer?.currentPosition?.toLong() ?: 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun playSong(song: Song) {
        dispose()
        prepare(song, true)
    }

    override fun seek(ms: Long) {
        mediaPlayer?.seekTo(ms.toInt())
    }

    override fun pauseOrPlay() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {           // pause
                progressWatcher.interrupt()
                player.pause()
            }
            else { // play
                player.start()
                prepareWatcher()
                progressWatcher.start()
            }
        }
    }

    override fun stop() {
        mediaPlayer?.pause()
        seek(0)
        progressWatcher.interrupt()
    }

    override fun dispose() {
        progressWatcher.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun prepareWatcher() {
        progressWatcher.interrupt()
        progressWatcher = progressWatcher.newWorker { produceState() }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun prepare(input: Song, playAfter: Boolean) {
        dispose()
        playAfterPrepare = playAfter
        duration = input.duration
        mediaPlayer = MediaPlayer.create(appContext, input.filepath).apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnPreparedListener(this@AudioPlayer)
            setOnCompletionListener(this@AudioPlayer)
            setOnSeekCompleteListener(this@AudioPlayer)
        }
    }

    override fun onPrepared(mp: MediaPlayer) {
        prepareWatcher()
        seek(0)
        if (playAfterPrepare) {
            mp.start()
            progressWatcher.start()
        }
        playAfterPrepare = true
    }

    override fun onCompletion(mp: MediaPlayer?) {
        progressWatcher.interrupt()
        playbackState.update(PlaybackState(0L, duration, true))
        seek(0)
    }

    override fun onSeekComplete(mp: MediaPlayer) {
        produceState()
    }

    private fun produceState() = playbackState.update(PlaybackState(position, duration))

    override fun close() = dispose()
}