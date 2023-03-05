package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.domain.*

class PlayerStateCache : InMemoryCache<PlayerState> {
    var currentTrack: Song = Song(-1L, "", "", 0)
    var mode: Mode = Mode.PLAY_MUSIC
    var currentPlaylist: Playlist = Playlist(-1, "", emptyList())
    var playlists: List<Playlist> = emptyList()
    var selectedSongs: MutableSet<Long> = mutableSetOf()
    var allSongs: List<Song> = emptyList()

    override fun read(): PlayerState {
        return PlayerState(
            currentTrack = currentTrack,
            mode = mode,
            currentPlaylist = currentPlaylist,
            playlists = playlists,
            selectedSongs = selectedSongs,
            allSongs = allSongs
        )
    }

    override fun save(data: PlayerState): PlayerState {
        save(
            data.currentTrack,
            data.mode,
            data.currentPlaylist,
            data.playlists,
            data.selectedSongs,
            data.allSongs
        )
        return read()
    }

    private fun save(
        currentTrack: Song = this.currentTrack,
        mode: Mode = this.mode,
        currentPlaylist: Playlist = this.currentPlaylist,
        playlists: List<Playlist> = this.playlists,
        selectedSongs: MutableSet<Long> = this.selectedSongs,
        allSongs: List<Song> = this.allSongs
    ) {
        this.currentTrack = currentTrack
        this.mode = mode
        this.currentPlaylist = currentPlaylist
        this.playlists = playlists
        this.selectedSongs = selectedSongs
        this.allSongs = allSongs
    }
}