package org.hyperskill.musicplayer.ui.mapper

import java.util.concurrent.TimeUnit

interface StringFormatter<T> {
    fun format(input: T) : String

    class MillisToTime : StringFormatter<Long> {
        private val pattern: String = "%02d:%02d"
        override fun format(input: Long): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(input)
            return String.format(
                pattern,
                minutes,
                TimeUnit.MILLISECONDS.toSeconds(input) - TimeUnit.MINUTES.toSeconds(minutes)
            )
        }
    }
}