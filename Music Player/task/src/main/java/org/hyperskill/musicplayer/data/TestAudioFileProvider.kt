package org.hyperskill.musicplayer.data


import android.content.Context
import android.net.Uri

private const val TEST_DURATION = 215_000L
class TestAudioFileProvider(private val context: Context, private val rawFileName: String) {
    fun provide(): Pair<Uri, Long> {
        return Uri.parse(
            "android.resource://${context.packageName}/raw/${
                rawFileName.substringBeforeLast(".")
            }"
        ) to TEST_DURATION
    }
}
