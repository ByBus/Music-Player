package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.*

class DeletePlaylistUseCase(
    private val repository: Repository,
    private val playerState: InMemoryCache<PlayerState>
) : UseCase<Playlist, PlayerState> {
    override fun invoke(data: Playlist): PlayerState {
        repository.deletePlaylist(data)
        var state = playerState.read()
        val playlists = repository.playlists()
        if (state.currentPlaylist.id == data.id || state.mode == Mode.ADD_PLAYLIST) {
            val songs = repository.allSongs()
            state = state.copy(allSongs = songs, currentPlaylist = playlists.first())
        }
        state.playlists = playlists
        state.updateSelection()
        return playerState.save(state)
    }
}