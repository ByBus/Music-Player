package org.hyperskill.musicplayer.ui

abstract class PlaybackStateUi(
    val current: Int,
    val max: Int,
    val currentTime: String,
    val durationTime: String,
) {
    abstract fun complete(handler: CompletionHandler)

    class Base(
        current: Int,
        max: Int,
        currentTime: String,
        durationTime: String,
    ) : PlaybackStateUi(current, max, currentTime, durationTime) {
        override fun complete(handler: CompletionHandler) = Unit
    }

    class Finished(
        current: Int,
        max: Int,
        currentTime: String,
        durationTime: String,
    ) : PlaybackStateUi(current, max, currentTime, durationTime) {
        override fun complete(handler: CompletionHandler) {
            handler.onCompletion()
        }
    }
}