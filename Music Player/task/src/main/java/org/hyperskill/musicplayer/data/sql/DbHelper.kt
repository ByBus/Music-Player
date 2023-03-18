package org.hyperskill.musicplayer.data.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(
    context: Context,
    name: String
) : SQLiteOpenHelper(context, name, null, 1) {

    override fun onConfigure(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = 1")
        db.execSQL("PRAGMA trusted_schema = 0")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS playlist(" +
                    "  playlistName INTEGER" +
                    ", songId TEXT NOT NULL" +
                    ", PRIMARY KEY (playlistName, songId)" +
                    ")"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int, newVersion: Int,
    ) {
        throw UnsupportedOperationException()
    }
}