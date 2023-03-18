package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.data.sql.PlaylistDB

interface PlaylistDBDao<T> {

    fun findAll(): List<PlaylistDB>

    fun findById(id: T): PlaylistDB?

    fun findByName(name: String): PlaylistDB?

    fun delete(id: T)

    fun insert(playlist: PlaylistDB): T

    fun update(playlist: PlaylistDB)
}