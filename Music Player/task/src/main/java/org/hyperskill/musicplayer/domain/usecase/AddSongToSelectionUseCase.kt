package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.InMemoryCache
import org.hyperskill.musicplayer.domain.PlayerState
import org.hyperskill.musicplayer.domain.UseCase

class AddSongToSelectionUseCase(
    private val playerState: InMemoryCache<PlayerState>
) : UseCase<Long, PlayerState> {
    override fun invoke(data: Long): PlayerState {
        val state = playerState.read()
        val selected = state.selectedSongs.toMutableSet()
        val isAdded = selected.add(data)
        if (!isAdded) {
            selected.remove(data)
        }
        state.updateSelection(selected)
        return playerState.save(state)
    }
}