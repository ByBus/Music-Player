package org.hyperskill.musicplayer.ui.mapper

import androidx.fragment.app.Fragment
import org.hyperskill.musicplayer.domain.Mode
import org.hyperskill.musicplayer.domain.Playlist
import org.hyperskill.musicplayer.ui.MainAddPlaylistFragment
import org.hyperskill.musicplayer.ui.MainPlayerControllerFragment
import org.hyperskill.musicplayer.ui.SongUi

interface UiStateMapper<T> {
    operator fun invoke(state: Mode, songs: List<SongUi>, playlists: List<Playlist>): T

    class ToFragment : UiStateMapper<Class<out Fragment>> {
        override fun invoke(
            state: Mode,
            songs: List<SongUi>,
            playlists: List<Playlist>
        ): Class<out Fragment> {
            return when (state) {
                Mode.PLAY_MUSIC -> MainPlayerControllerFragment::class.java
                Mode.ADD_PLAYLIST -> MainAddPlaylistFragment::class.java
            }
        }
    }
}
