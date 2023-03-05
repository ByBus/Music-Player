package org.hyperskill.musicplayer.domain

data class Playlist(val id: Long, val title: String, val songsIds: List<Long>, val default: Boolean = false)