package org.hyperskill.musicplayer.data

import android.net.Uri
import org.hyperskill.musicplayer.domain.Song

interface SongDBMapper<T> {
    operator fun invoke(
        id: Long,
        title: String,
        artist: String,
        duration: Long,
        path: String
    ): T

    class ToDomain: SongDBMapper<Song> {
        override fun invoke(
            id: Long,
            title: String,
            artist: String,
            duration: Long,
            path: String
        ): Song {
            return Song(id, title, artist, duration, filepath = Uri.parse(path))
        }
    }
}
