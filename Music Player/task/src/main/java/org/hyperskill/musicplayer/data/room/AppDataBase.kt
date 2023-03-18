package org.hyperskill.musicplayer.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaylistAndSongId::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun dao() : PlayListAndSongIdDao
}