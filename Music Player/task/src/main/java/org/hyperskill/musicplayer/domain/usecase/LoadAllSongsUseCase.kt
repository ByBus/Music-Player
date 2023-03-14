package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.*

class LoadAllSongsUseCase(
    private val repository: Repository,
    private val playerState: InMemoryCache<PlayerState>,
    private val player: Player
) : UseCase<String, PlayerState> {
    override fun invoke(data: String): PlayerState {
        val state = playerState.read()
        val songs = repository.allSongs()
        val newCurrentPlaylist = when (state.mode) {
            Mode.PLAY_MUSIC -> repository.createPlaylist(title = data, songs.map { it.id }, true)
            Mode.ADD_PLAYLIST -> state.currentPlaylist
        }
        with(state) {
            allSongs = songs
            playlists = repository.playlists()
            currentPlaylist = newCurrentPlaylist
            currentTrack = songs.first()
        }
        player.prepare(state.currentTrack, playAfter = false)
        return playerState.save(state)
    }
}