package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.UseCase
import org.hyperskill.musicplayer.domain.Player

class SetSongProgressUseCase(
    private val player: Player
) : UseCase<Int, Unit> {
    override fun invoke(data: Int) {
        player.seek(data.toLong())
    }
}