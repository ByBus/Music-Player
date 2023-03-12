package org.hyperskill.musicplayer.data

interface DataSource<T, ID> {
    fun readAll(): List<T>
    fun findById(id: ID): T?
    fun findByIds(ids: List<ID>): List<T>
    fun findByName(name: String): T?
}

interface MutableDataSource<T, ID, T2> : DataSource<T, ID> {
    fun deleteById(id: ID)
    fun update(item: T, data: List<T2>): T?
    fun save(item: T): T
}