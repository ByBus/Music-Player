package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.*

class ReturnToPlayStateUseCase(
    private val repository: Repository,
    private val playerState: InMemoryCache<PlayerState>
) : UnitUseCase<PlayerState> {
    override fun invoke(): PlayerState {
        val state = playerState.read()
        with(state) {
            updateSelection(emptyList())
            allSongs = repository.songsOfPlaylist(state.currentPlaylist)
            mode = Mode.PLAY_MUSIC
            updateSelection()
        }
        return playerState.save(state)
    }
}