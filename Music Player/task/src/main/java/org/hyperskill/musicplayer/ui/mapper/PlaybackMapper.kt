package org.hyperskill.musicplayer.ui.mapper

import org.hyperskill.musicplayer.ui.PlaybackStateUi

interface PlaybackMapper<T> {
    operator fun invoke(current: Long, duration: Long, isFinished: Boolean): T

    class ToUi(private val stringFormatter: StringFormatter<Long>) :
        PlaybackMapper<PlaybackStateUi> {
        override fun invoke(current: Long, duration: Long, isFinished: Boolean): PlaybackStateUi {
            val currentTime = stringFormatter.format(current)
            val durationTime = stringFormatter.format(duration)
            val currentSec = (current / 1000).toInt()
            val durationSec = (duration / 1000).toInt()
            return if (isFinished) {
                PlaybackStateUi.Finished(currentSec, durationSec, currentTime, durationTime)
            } else {
                PlaybackStateUi.Base(currentSec, durationSec, currentTime, durationTime)
            }
        }
    }
}