package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.*

class LoadPlaylistSongsUseCase(
    private val repository: Repository,
    private val playerState: InMemoryCache<PlayerState>,
    private val player: Player
) : UseCase<Playlist, PlayerState> {
    override fun invoke(data: Playlist): PlayerState {
        val state = playerState.read()
        val newCurrentPlaylist = when (state.mode) {
            Mode.PLAY_MUSIC -> data
            Mode.ADD_PLAYLIST -> state.currentPlaylist
        }
        val songs = repository.songsOfPlaylist(data)
        val newCurrentTrack =
            if (songs.any { it.matches(state.currentTrack) } || state.mode == Mode.ADD_PLAYLIST) {
                state.currentTrack
            } else {
                songs.firstOrNull()
            }

        with(state) {
            allSongs = songs
            currentPlaylist = newCurrentPlaylist
            newCurrentTrack?.let {
                if (state.currentTrack.matches(it).not()) player.prepare(it, false)
                currentTrack = it
            }
            updateSelection()
        }
        return playerState.save(state)
    }
}