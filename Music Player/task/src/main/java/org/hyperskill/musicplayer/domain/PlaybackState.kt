package org.hyperskill.musicplayer.domain

import org.hyperskill.musicplayer.ui.mapper.PlaybackMapper

data class PlaybackState(val current: Long, val duration: Long, val isFinished: Boolean = false) {
    fun <T> map(mapper: PlaybackMapper<T>) : T {
        return mapper(current, duration, isFinished)
    }
}

