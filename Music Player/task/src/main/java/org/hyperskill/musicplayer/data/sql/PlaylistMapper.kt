package org.hyperskill.musicplayer.data.sql

import org.hyperskill.musicplayer.domain.Playlist

interface PlaylistMapper<T> {
    operator fun invoke(id: Long, name: String, songIds: List<Long>) : T

    class ToDomain : PlaylistMapper<Playlist> {
        override fun invoke(id: Long, name: String, songIds: List<Long>): Playlist {
            return Playlist(id, name, songIds)
        }
    }

    class ToDB : PlaylistMapper<PlaylistDB> {
        override fun invoke(id: Long, name: String, songIds: List<Long>): PlaylistDB {
            return PlaylistDB(id, name, songIds)
        }
    }
}