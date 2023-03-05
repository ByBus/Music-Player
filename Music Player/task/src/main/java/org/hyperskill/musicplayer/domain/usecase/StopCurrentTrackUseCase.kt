package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.InMemoryCache
import org.hyperskill.musicplayer.domain.PlayerState
import org.hyperskill.musicplayer.domain.UnitUseCase

class StopCurrentTrackUseCase(
    private val playerState: InMemoryCache<PlayerState>
) : UnitUseCase<PlayerState> {
    override fun invoke(): PlayerState {
        val state = playerState.read()
        state.currentTrack.stop()
        return playerState.save(state)
    }
}