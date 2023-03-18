package org.hyperskill.musicplayer.domain

import org.hyperskill.musicplayer.ui.mapper.SongMapper
import org.hyperskill.musicplayer.ui.UiState

interface PlayerStateMapper<T> {
    operator fun invoke(
        currentTrack: Song,
        mode: Mode,
        currentPlaylist: Playlist,
        playlists: List<Playlist>,
        selectedSongs: MutableSet<Long>,
        allSongs: List<Song>,
        initial: Boolean
    ): T

    class ToUi : PlayerStateMapper<UiState> {
        override fun invoke(
            currentTrack: Song,
            mode: Mode,
            currentPlaylist: Playlist,
            playlists: List<Playlist>,
            selectedSongs: MutableSet<Long>,
            allSongs: List<Song>,
            initial: Boolean
        ): UiState {
            val selected = allSongs.map { it.id }.intersect(selectedSongs)
            val updatedSongs = mutableListOf<Song>()
            for (song in allSongs) {
                val state = if (song.id == currentTrack.id) currentTrack.trackState else TrackState.STOPPED
                val isSelected = song.id in selected
                updatedSongs.add(song.copy(trackState = state, selected = isSelected))
            }
            val songMapper = SongMapper.ToUi(mode)
            val uiSongs = updatedSongs.map { it.map(songMapper) }
            return if (initial)
                UiState.Initial(mode, uiSongs, playlists)
            else
                UiState.Base(mode, uiSongs, playlists)
        }
    }
}