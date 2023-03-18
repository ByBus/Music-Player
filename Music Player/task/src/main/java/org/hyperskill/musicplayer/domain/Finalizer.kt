package org.hyperskill.musicplayer.domain

import java.io.Closeable

interface Finalizer {
    fun finalizeAll()

    class ResourceReleaser(private vararg val systemResources: Closeable) : Finalizer {
        override fun finalizeAll() = systemResources.forEach(Closeable::close)
    }
}