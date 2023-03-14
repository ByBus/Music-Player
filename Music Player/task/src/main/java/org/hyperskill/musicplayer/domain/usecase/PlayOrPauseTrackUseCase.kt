package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.InMemoryCache
import org.hyperskill.musicplayer.domain.PlayerState
import org.hyperskill.musicplayer.domain.UseCase
import org.hyperskill.musicplayer.domain.Player

class PlayOrPauseTrackUseCase(
    private val playerState: InMemoryCache<PlayerState>,
    private val player: Player
) : UseCase<Long, PlayerState> {
    override fun invoke(data: Long): PlayerState {
        val state = playerState.read()
        val song = state.currentTrack
        val currentTrack = if (song.id == data) {
            song.switchPlaying()
            player.pauseOrPlay()
            song
        } else {
            val newSong = state.allSongs.firstOrNull { it.id == data }!!
            newSong.play()
            player.playSong(newSong)
            newSong
        }
        state.currentTrack = currentTrack
        return playerState.save(state)
    }
}