package org.hyperskill.musicplayer.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "playlist", primaryKeys = ["playlistName", "songId"])
class PlaylistAndSongId(
    @ColumnInfo(name = "playlistName")
    var name: String,
    @ColumnInfo(name = "songId")
    var songId: Long
)