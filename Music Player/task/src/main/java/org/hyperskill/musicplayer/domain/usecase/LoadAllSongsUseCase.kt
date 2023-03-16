package org.hyperskill.musicplayer.domain.usecase

import org.hyperskill.musicplayer.domain.*

class LoadAllSongsUseCase(
    private val repository: Repository,
    private val loadPlaylistSongsUseCase: UseCase<Playlist, PlayerState>
) : UseCase<String, PlayerState> {
    override fun invoke(data: String): PlayerState {
        val songs = repository.allSongs()
        val newCurrentPlaylist = repository.createPlaylist(title = data, songs.map { it.id }, true)
        return loadPlaylistSongsUseCase(newCurrentPlaylist)
    }
}