package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.data.sql.PlaylistMapper
import org.hyperskill.musicplayer.domain.Playlist

class PlayListLocalDataSource(
    private val dao: PlaylistDBDao<String>,
    private val dbMapper: PlaylistMapper.ToDB,
    private val domainMapper: PlaylistMapper.ToDomain,
) : MutableDataSource<Playlist, String> {
    override fun readAll(): List<Playlist> = dao.findAll().map { it.map(domainMapper) }

    override fun findById(id: String): Playlist? = dao.findById(id)?.map(domainMapper)

    override fun findByName(name: String): Playlist? = dao.findByName(name)?.map(domainMapper)

    override fun deleteById(id: String) {
        dao.delete(id)
    }

    override fun update(item: Playlist): Playlist? {
        dao.findById(item.title)?.let {
            dao.update(item.map(dbMapper))
            return item.copy()
        }
        return null
    }

    override fun save(item: Playlist): Playlist {
        val id = dao.insert(item.map(dbMapper))
        return findById(id)!!
    }
}