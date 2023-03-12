package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.InMemoryCache
import org.hyperskill.musicplayer.domain.PlayerState
import org.hyperskill.musicplayer.domain.UnitUseCase
import org.hyperskill.musicplayer.domain.PlayerController

class StopCurrentTrackUseCase(
    private val playerState: InMemoryCache<PlayerState>,
    private val player: PlayerController
) : UnitUseCase<PlayerState> {
    override fun invoke(): PlayerState {
        val state = playerState.read()
        state.currentTrack.stop()
        player.stop()
        return playerState.save(state)
    }
}