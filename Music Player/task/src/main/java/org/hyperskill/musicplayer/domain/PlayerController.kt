package org.hyperskill.musicplayer.domain


interface PlayerController {
    fun playSong(song: Song)
    fun seek(ms: Long)
    fun pauseOrPlay()
    fun stop()
    fun dispose()
    fun prepare(input: Song, playAfter: Boolean)
}