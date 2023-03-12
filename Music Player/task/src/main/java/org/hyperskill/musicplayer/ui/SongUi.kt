package org.hyperskill.musicplayer.ui

import org.hyperskill.musicplayer.R
import org.hyperskill.musicplayer.ui.mapper.SongMapper

abstract class SongUi(
    open val id: Long,
    open val title: String,
    open val artist: String,
    open val duration: Long,
) {
    fun <T> map(mapper: SongMapper<T>): T {
        return mapper(id, title, artist, duration, state(), state())
    }

    abstract fun layout() : Int

    abstract fun state() : Boolean

    fun isSameAs(other: SongUi): Boolean {
        return this.layout() == other.layout() && this.id == other.id
    }

    data class SongUiBase(
        override val id: Long,
        override val title: String,
        override val artist: String,
        override val duration: Long,
        private val playing: Boolean
    ) : SongUi(id, title, artist, duration) {
        override fun layout() = R.layout.list_item_song
        override fun state() = playing
    }

    data class SongUiSelector(
        override val id: Long,
        override val title: String,
        override val artist: String,
        override val duration: Long,
        private val selected: Boolean,
    ) : SongUi(id, title, artist, duration) {
        override fun layout() = R.layout.list_item_song_selector
        override fun state() = selected
    }
}