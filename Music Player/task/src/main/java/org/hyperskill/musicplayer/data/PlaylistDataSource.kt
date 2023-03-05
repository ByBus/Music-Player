package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.domain.Playlist


class PlaylistDataSource {
    private val playlists = mutableListOf<Playlist>()

    fun readAll(): List<Playlist> = playlists.toList()

    fun findById(id: Long): Playlist? = playlists.firstOrNull { it.id == id }

    fun findByName(name: String): Playlist? = playlists.firstOrNull { it.title == name }

    fun deleteById(id: Long) {
        findById(id)?.let{
            playlists.remove(it)
        }
    }

    fun update(id: Long, songsIds: List<Long>): Playlist? {
        findById(id)?.let {
            val index = playlists.indexOf(it)
            val oldPlaylist = playlists[index]
            playlists[index] = oldPlaylist.copy(songsIds = songsIds, default = oldPlaylist.default)
            return playlists[index]
        }
        return null
    }

    fun create(playlist: Playlist): Playlist {
        playlists.add(playlist)
        return playlist
    }
}