package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.domain.Playlist


class PlaylistDataSource : MutableDataSource<Playlist, Long, Long>{
    private val playlists = mutableListOf<Playlist>()

    override fun readAll(): List<Playlist> = playlists.toList()

    override fun findById(id: Long): Playlist? = playlists.firstOrNull { it.id == id }

    override fun findByName(name: String): Playlist? = playlists.firstOrNull { it.title == name }

    override fun deleteById(id: Long) {
        findById(id)?.let{
            playlists.remove(it)
        }
    }

    override fun update(item: Playlist, data: List<Long>): Playlist? {
        findById(item.id)?.let {
            val index = playlists.indexOf(it)
            val oldPlaylist = playlists[index]
            playlists[index] = oldPlaylist.copy(songsIds = data)
            return playlists[index]
        }
        return null
    }

    override fun save(item: Playlist): Playlist {
        playlists.add(item)
        return item
    }

    override fun findByIds(ids: List<Long>): List<Playlist> {
        return playlists.filter { it.id in ids }
    }
}