package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.*

class CreatePlaylistUseCase(
    private val repository: Repository,
    private val playerState: InMemoryCache<PlayerState>
) : UseCase<String, PlayerState> {
    override fun invoke(data: String): PlayerState {
        val state = playerState.read()
        repository.createPlaylist(title = data, state.selectedSongs.toList())
        with(state) {
            playlists = repository.playlists()
            updateSelection(emptyList())
            mode = Mode.PLAY_MUSIC
        }
        return playerState.save(state)
    }
}