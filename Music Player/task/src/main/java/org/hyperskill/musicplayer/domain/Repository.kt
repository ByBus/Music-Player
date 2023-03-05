package org.hyperskill.musicplayer.domain

interface Repository {
    fun playlists(): List<Playlist>
    fun songsOfPlaylist(playlist: Playlist): List<Song>
    fun allSongs(): List<Song>
    fun deletePlaylist(playlist: Playlist)
    fun addSongsToPlaylist(playlist: Playlist, songs: List<Song>)
    fun createPlaylist(title: String, songsIds: List<Long>, default: Boolean = false): Playlist
}