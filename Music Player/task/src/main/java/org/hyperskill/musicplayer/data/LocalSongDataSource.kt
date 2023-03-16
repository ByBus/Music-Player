package org.hyperskill.musicplayer.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import org.hyperskill.musicplayer.domain.Song

class LocalSongDataSource(private val context: Context) : DataSource<Song, Long> {
    private val contentResolver get() = context.contentResolver
    private val songs = mutableSetOf<Song>()

    override fun readAll(): List<Song> {
        val queryFields =
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
            )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(
            externalContentUri,
            queryFields,
            selection,
            null,
            null
            )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(0)
                songs.add(Song(
                    id = id,
                    title = it.getString(1),
                    artist = it.getString(2),
                    duration = it.getLong(3),
                    filepath = ContentUris.withAppendedId(externalContentUri, id)
                ))
            }
        }
        return songs.toList()
    }

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