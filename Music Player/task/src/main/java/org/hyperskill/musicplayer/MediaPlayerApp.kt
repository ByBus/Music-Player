package org.hyperskill.musicplayer

import android.app.Application
import org.hyperskill.musicplayer.sl.DependencyContainer

class MediaPlayerApp : Application() {
    val dependencyContainer by lazy {DependencyContainer.Base(this)}
}