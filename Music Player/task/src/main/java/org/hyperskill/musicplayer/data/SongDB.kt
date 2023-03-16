package org.hyperskill.musicplayer.data

data class SongDB(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val path: String
) {
    fun<T> map (mapper: SongDBMapper<T>) : T {
        return mapper(id, title, artist, duration, path)
    }
}