package org.hyperskill.musicplayer.ui

import org.hyperskill.musicplayer.domain.Mode
import org.hyperskill.musicplayer.domain.Playlist

data class UiState(
    val state: Mode,
    val songs: List<SongUi>,
    val playlists: List<Playlist>
) {
    fun isPlaying(): Boolean {
        return state == Mode.PLAY_MUSIC
    }

    fun isSelecting(): Boolean {
        return state == Mode.ADD_PLAYLIST
    }

    fun <T> map(mapper: UiStateMapper<T>): T {
        return mapper(state, songs, playlists)
    }
}