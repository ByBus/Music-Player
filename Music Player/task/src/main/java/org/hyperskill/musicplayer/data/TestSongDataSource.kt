package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.domain.Song

class TestSongDataSource(testAudioFile: TestAudioFileProvider) : DataSource<Song, Long> {
    private val songs = mutableListOf<Song>()

    init {
        val (uri, duration) = testAudioFile.provide()
        val song = Song(id = 1, title = "title", artist = "artist", duration = duration, filepath = uri)
        for (i in 1..10) {
            songs.add(song.copy(id = i.toLong(), title = "${song.title}$i", artist = "${song.artist}$i"))
        }
    }

    override fun readAll() : List<Song> = songs.toList()

    override fun findById(id: Long): Song? {
        return songs.firstOrNull { it.id == id }
    }

    override fun findByIds(ids: List<Long>): List<Song> {
        return songs.filter { it.id in ids }
    }

    override fun findByName(name: String): Song? {
        return songs.firstOrNull { it.title == name }
    }
}