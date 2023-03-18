package org.hyperskill.musicplayer.data.sql

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.annotation.RequiresApi
import org.hyperskill.musicplayer.data.PlaylistDBDao
import java.io.Closeable

class PlaylistStore(private val db: SQLiteDatabase) : Closeable, PlaylistDBDao<String> {

    override fun insert(playlist: PlaylistDB): String {
        db.beginTransaction()
        try {
            if (findByName(playlist.name) == null) {
                insertSongs(playlist)
            } else {
                update(playlist)
            }
            db.setTransactionSuccessful()
            return playlist.name
        } finally {
            db.endTransaction()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun findAll(): List<PlaylistDB> {
        val cursorPlaylist = db.query(
            "playlist", arrayOf("playlistName", "songId"), null, null,
            null, null, null
        )
        val nameToSongIds = mutableMapOf<String, MutableList<Long>>()
        cursorPlaylist.use { c ->
            while (c.moveToNext()) {
                val playlistName = c.getString(0)
                val songId = c.getLong(1)
                nameToSongIds.merge(playlistName, mutableListOf(songId)) { old, new ->
                    old.apply { addAll(new) }
                }
            }
        }
        return nameToSongIds.map { (name, ids) ->
            PlaylistDB(name = name, songIds = ids)
        }
    }

    override fun findById(id: String): PlaylistDB? = findByName(id)

    override fun findByName(name: String): PlaylistDB? {
        val songIds = mutableListOf<Long>()
        val cursorSongs = db.query(
            "playlist", arrayOf("playlistName", "songId"), "playlistName = ?", arrayOf(name),
            null, null, null
        )
        cursorSongs.use { c ->
            while (c.moveToNext()) {
                songIds.add(c.getLong(1))
            }
        }
        return if (songIds.isEmpty()) null else PlaylistDB(name = name, songIds = songIds)
    }

    override fun delete(id: String) {
        db.beginTransaction()
        try {
            db.delete("playlist", "playlistName = ?", arrayOf(id))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun update(playlist: PlaylistDB) {
        db.beginTransaction()
        try {
            db.delete("playlist", "playlistName = ?", arrayOf(playlist.name))
            insertSongs(playlist)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    private fun insertSongs(playlist: PlaylistDB) {
        val rowSongId = ContentValues()
        for (songId in playlist.songIds) {
            rowSongId.put("playlistName", playlist.name)
            rowSongId.put("songId", songId)
            db.insert("playlist", null, rowSongId)
        }
    }

    override fun close() = db.close()
}