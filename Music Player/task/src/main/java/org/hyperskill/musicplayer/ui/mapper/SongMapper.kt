package org.hyperskill.musicplayer.ui.mapper

import org.hyperskill.musicplayer.domain.Mode
import org.hyperskill.musicplayer.ui.SongUi

interface SongMapper<T> {
    operator fun invoke(
        id: Long,
        title: String,
        artist: String,
        duration: Long,
        selected: Boolean,
        playing: Boolean,
    ): T

    class DurationMapper(private val formatter: StringFormatter<Long>) : SongMapper<String> {
        override fun invoke(
            id: Long,
            title: String,
            artist: String,
            duration: Long,
            selected: Boolean,
            playing: Boolean,
        ): String {
            return formatter.format(duration)
        }
    }

    class ToUi(private val mode: Mode) : SongMapper<SongUi> {
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