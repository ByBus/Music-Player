package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.*

class ChangePlayerModeUseCase(
    private val playerState: InMemoryCache<PlayerState>
) : CombinedUseCase<Long, PlayerState> {
    override fun invoke(data: Long): PlayerState {
        val state = playerState.read()
        when (state.mode) {
            Mode.PLAY_MUSIC -> {
                state.updateSelection(listOf(data))
                state.mode = Mode.ADD_PLAYLIST
            }
            Mode.ADD_PLAYLIST -> {
                state.updateSelection(emptyList())
                state.mode = Mode.PLAY_MUSIC
            }
        }
        return playerState.save(state)
    }

    override fun invoke(): PlayerState {
        val state = playerState.read()
        when (state.mode) {
            Mode.PLAY_MUSIC -> state.mode = Mode.ADD_PLAYLIST
            Mode.ADD_PLAYLIST -> state.mode = Mode.PLAY_MUSIC
        }
        return playerState.save(state)
    }
}