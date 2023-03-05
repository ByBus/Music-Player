package org.hyperskill.musicplayer.domain

import org.hyperskill.musicplayer.ui.SongMapper

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    private var selected: Boolean = false,
    var trackState: TrackState = TrackState.STOPPED
) {
    fun <T> map(mapper: SongMapper<T>): T {
        return mapper(id, title, artist, duration, selected, isPlaying())
    }

    fun stop(){
        trackState = TrackState.STOPPED
    }

    fun play() {
        trackState = TrackState.PLAYING
    }

    fun switchPlaying() {
        trackState = when (trackState) {
            TrackState.PLAYING -> TrackState.PAUSED
            else -> TrackState.PLAYING
        }
    }

    fun isPlaying() = trackState == TrackState.PLAYING
}

enum class TrackState {
    PLAYING, PAUSED, STOPPED
}



