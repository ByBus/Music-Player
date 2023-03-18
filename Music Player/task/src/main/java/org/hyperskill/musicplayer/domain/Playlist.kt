package org.hyperskill.musicplayer.domain

import org.hyperskill.musicplayer.data.sql.PlaylistMapper
data class Playlist(
    val id: Long,
    val title: String,
    val songsIds: List<Long>,
    val default: Boolean = false
) {
    fun <T> map(mapper: PlaylistMapper<T>): T {
        return mapper(id, title, songsIds)
    }
}

