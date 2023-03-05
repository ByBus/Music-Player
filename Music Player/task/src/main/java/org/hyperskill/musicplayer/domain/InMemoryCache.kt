package org.hyperskill.musicplayer.domain

interface InMemoryCache<T> {
    fun read() : T
    fun save(data: T) : T
}