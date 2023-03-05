package org.hyperskill.musicplayer.domain

import org.hyperskill.musicplayer.ui.SongUi
import java.util.concurrent.TimeUnit

interface SongMapper<T> {
    operator fun invoke(
        id: Long,
        title: String,
        artist: String,
        duration: Long,
        selected: Boolean,
        playing: Boolean,
    ): T

    class DurationMapper : SongMapper<String> {
        override fun invoke(
            id: Long,
            title: String,
            artist: String,
            duration: Long,
            selected: Boolean,
            playing: Boolean,
        ): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
            return String.format(
                "%02d:%02d",
                minutes,
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
            )
        }
    }

    class SongToUiMapper(private val mode: Mode) : SongMapper<SongUi> {
        override fun invoke(
            id: Long,
            title: String,
            artist: String,
            duration: Long,
            selected: Boolean,
            playing: Boolean,
        ): SongUi {
            return when (mode) {
                Mode.PLAY_MUSIC -> SongUi.SongUiBase(id, title, artist, duration, playing)
                Mode.ADD_PLAYLIST -> SongUi.SongUiSelector(id, title, artist, duration, selected)
            }
        }
    }
}