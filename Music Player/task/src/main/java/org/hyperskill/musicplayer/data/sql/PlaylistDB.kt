package org.hyperskill.musicplayer.data.sql

class PlaylistDB(
    val id: Long = 1,
    val name: String,
    val songIds: List<Long>
) {

    fun <T> map(mapper: PlaylistMapper<T>): T {
        return mapper(id, name, songIds)
    }
}