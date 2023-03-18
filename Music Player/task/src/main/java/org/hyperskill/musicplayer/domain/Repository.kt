package org.hyperskill.musicplayer.domain

interface Repository : ReadRepository {
    fun songsOfPlaylist(playlist: Playlist): List<Song>
    fun deletePlaylist(playlist: Playlist)
    fun createPlaylist(title: String, songsIds: List<Long>, default: Boolean = false): Playlist
}

interface ReadRepository {
    fun playlists(): List<Playlist>
    fun allSongs(): List<Song>
}