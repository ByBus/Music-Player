package org.hyperskill.musicplayer.ui

import org.hyperskill.musicplayer.domain.Mode
import org.hyperskill.musicplayer.domain.Playlist
import org.hyperskill.musicplayer.ui.mapper.UiStateMapper

data class UiState(
    val state: Mode,
    val songs: List<SongUi>,
    val playlists: List<Playlist>
) {
    fun <T> map(mapper: UiStateMapper<T>): T {
        return mapper(state, songs, playlists)
    }
}