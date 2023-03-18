package org.hyperskill.musicplayer.data.room

import org.hyperskill.musicplayer.data.MutableDataSource
import org.hyperskill.musicplayer.domain.Playlist

class PlaylistDbLocalDataSource(private val dao: PlayListAndSongIdDao) :
    MutableDataSource<Playlist, String> {
    override fun readAll(): List<Playlist> {
        return dao.findAll().groupingBy { it.name }
            .aggregate { _, accumulator: MutableList<Long>?, element, first ->
                if (first)
                    mutableListOf(element.songId)
                else
                    accumulator?.apply { add(element.songId) }
            }.map { (name, ids) ->
                Playlist(0, title = name, songsIds = ids ?: emptyList())
            }
    }

    override fun findById(id: String): Playlist? = findByName(id)

    override fun findByName(name: String): Playlist? {
        val ids = dao.findByName(name).map { it.songId }
        return if (ids.isEmpty()) null else Playlist(0, name, ids)
    }

    override fun deleteById(id: String) = dao.delete(id)

    override fun update(item: Playlist): Playlist {
        deleteById(item.title)
        return save(item)
    }

    override fun save(item: Playlist): Playlist {
        val rows = item.songsIds.map { PlaylistAndSongId(item.title, it) }
        dao.insert(rows)
        return item.copy()
    }
}