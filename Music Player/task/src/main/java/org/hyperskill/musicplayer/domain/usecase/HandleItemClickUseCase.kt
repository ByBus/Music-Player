package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.InMemoryCache
import org.hyperskill.musicplayer.domain.Mode
import org.hyperskill.musicplayer.domain.PlayerState
import org.hyperskill.musicplayer.domain.UseCase

class HandleItemClickUseCase(
    private val playOrPauseTrackUseCase: UseCase<Long, PlayerState>,
    private val addSongToSelectionUseCase: UseCase<Long, PlayerState>,
    private val playerState: InMemoryCache<PlayerState>
) : UseCase<Long, PlayerState> {
    override fun invoke(data: Long): PlayerState {
        return when (playerState.read().mode) {
            Mode.PLAY_MUSIC -> playOrPauseTrackUseCase(data)
            Mode.ADD_PLAYLIST -> addSongToSelectionUseCase(data)
        }
    }
}