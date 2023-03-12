package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.UseCase
import org.hyperskill.musicplayer.domain.PlayerController

class SetSongProgressUseCase(
    private val player: PlayerController
) : UseCase<Int, Unit> {
    override fun invoke(data: Int) {
        player.seek(data.toLong())
    }
}