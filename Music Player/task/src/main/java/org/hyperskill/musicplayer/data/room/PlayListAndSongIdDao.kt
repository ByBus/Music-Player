package org.hyperskill.musicplayer.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlayListAndSongIdDao {
    @Query("SELECT * FROM playlist")
    fun findAll(): List<PlaylistAndSongId>

    @Query("SELECT * FROM playlist WHERE playlistName = :name")
    fun findByName(name: String): List<PlaylistAndSongId>

    @Query("DELETE FROM playlist WHERE playlistName = :name")
    fun delete(name: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(playlist: List<PlaylistAndSongId>)
}