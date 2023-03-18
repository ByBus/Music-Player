package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.domain.Playlist

class InMemoryDefaultPlayListDataSource : DataSource<Playlist, Long> {
    private val defaultPlaylist = listOf(Playlist(0, "All Songs", emptyList(), true))

    override fun readAll(): List<Playlist>  = defaultPlaylist

    override fun findById(id: Long): Playlist = defaultPlaylist.first()

    override fun findByName(name: String): Playlist = defaultPlaylist.first()
}