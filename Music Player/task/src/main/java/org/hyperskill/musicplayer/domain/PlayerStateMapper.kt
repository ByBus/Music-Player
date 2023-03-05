package org.hyperskill.musicplayer.domain

import org.hyperskill.musicplayer.ui.SongMapper
import org.hyperskill.musicplayer.ui.UiState

interface PlayerStateMapper<T> {
    operator fun invoke(
        currentTrack: Song,
        mode: Mode,
        currentPlaylist: Playlist,
        playlists: List<Playlist>,
        selectedSongs: MutableSet<Long>,
        allSongs: List<Song>
    ): T

    class ToUiState : PlayerStateMapper<UiState> {
        override fun invoke(
            currentTrack: Song,
            mode: Mode,
            currentPlaylist: Playlist,
            playlists: List<Playlist>,
            selectedSongs: MutableSet<Long>,
            allSongs: List<Song>
        ): UiState {
            val selected = allSongs.map { it.id }.intersect(selectedSongs)
            val updatedSongs = mutableListOf<Song>()
            for (song in allSongs) {
                val state = if (song.id == currentTrack.id) currentTrack.trackState else TrackState.STOPPED
                val isSelected = song.id in selected
                updatedSongs.add(song.copy(trackState = state, selected = isSelected))
            }
            val songMapper = SongMapper.SongToUiMapper(mode)
            val uiSongs = updatedSongs.map { it.map(songMapper) }
            return UiState(mode, uiSongs, playlists)
        }
    }
}