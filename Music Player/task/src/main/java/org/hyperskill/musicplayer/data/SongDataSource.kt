package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.domain.Song

class SongDataSource {
    private val songs = mutableListOf<Song>()

    init {
        val song = Song(id = 1, title = "title", artist = "artist", duration = 215_000)
        for (i in 1..10) {
            songs.add(song.copy(id = i.toLong(), title = "${song.title}$i", artist = "${song.artist}$i"))
        }
    }

    fun readAll() : List<Song> = songs.toList()

    fun findById(id: Long): Song? {
        return songs.firstOrNull { it.id == id }
    }

    fun findByIds(songsIds: List<Long>): List<Song> {
        return songs.filter { it.id in songsIds }
    }
}