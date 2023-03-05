package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.*

class LoadPlaylistSongsUseCase(
    private val repository: Repository,
    private val playerState: InMemoryCache<PlayerState>
) : UseCase<Playlist, PlayerState> {
    override fun invoke(data: Playlist): PlayerState {
        val state = playerState.read()
        val newCurrentPlaylist = when (state.mode) {
            Mode.PLAY_MUSIC -> data
            Mode.ADD_PLAYLIST -> state.currentPlaylist
        }
        val songs = repository.songsOfPlaylist(data)
        val newCurrentTrack =
            if (songs.contains(state.currentTrack) || state.mode == Mode.ADD_PLAYLIST) {
                state.currentTrack
            } else {
                songs.first()
            }
        with(state) {
            currentPlaylist = newCurrentPlaylist
            allSongs = songs
            currentTrack = newCurrentTrack
        }
        state.updateSelection()
        return playerState.save(state)
    }
}