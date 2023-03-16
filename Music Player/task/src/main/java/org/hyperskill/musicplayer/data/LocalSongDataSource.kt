package org.hyperskill.musicplayer.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore

class LocalSongDataSource(private val context: Context) : DataSource<SongDB, Long> {
    private val contentResolver get() = context.contentResolver
    private val songs = mutableSetOf<SongDB>()

    override fun readAll(): List<SongDB> {
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
                songs.add(SongDB(
                    id = id,
                    title = it.getString(1),
                    artist = it.getString(2),
                    duration = it.getLong(3),
                    path = ContentUris.withAppendedId(externalContentUri, id).toString()
                ))
            }
        }
        return songs.toList()
    }

    override fun findById(id: Long): SongDB? {
        return songs.firstOrNull { it.id == id }
    }

    override fun findByIds(ids: List<Long>): List<SongDB> {
        return songs.filter { it.id in ids }
    }

    override fun findByName(name: String): SongDB? {
        return songs.firstOrNull { it.title == name }
    }
}