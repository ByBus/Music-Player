package org.hyperskill.musicplayer.ui

import org.hyperskill.musicplayer.domain.Mode
import org.hyperskill.musicplayer.domain.Playlist
import org.hyperskill.musicplayer.ui.mapper.UiStateMapper

abstract class UiState(
    val state: Mode,
    val songs: List<SongUi>,
    val playlists: List<Playlist>
) {
    val userPlaylists: List<Playlist>
        get() = playlists.filterNot { it.default }

    fun <T> map(mapper: UiStateMapper<T>): T {
        return mapper(state, songs, playlists)
    }

    abstract fun checkForWarning(handler: MessageHandler, warningText: String)

    class Initial(
        state: Mode,
        songs: List<SongUi>,
        playlists: List<Playlist>
    ) : UiState(state, songs, playlists) {
        override fun checkForWarning(handler: MessageHandler, warningText: String) = Unit
    }

    class Base(
        state: Mode,
        songs: List<SongUi>,
        playlists: List<Playlist>
    ) : UiState(state, songs, playlists) {
        override fun checkForWarning(handler: MessageHandler, warningText: String) {
            if (songs.isEmpty()) handler.handleMessage(warningText)
        }
    }
}

