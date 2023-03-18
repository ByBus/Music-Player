package org.hyperskill.musicplayer.data

interface DataSource<T, ID> {
    fun readAll(): List<T>
    fun findById(id: ID): T?
    fun findByName(name: String): T?
}

interface MultiIdsDataSource<T, ID> : DataSource<T, ID>{
    fun findByIds(ids: List<ID>): List<T>
}

interface MutableDataSource<T, ID> : DataSource<T, ID> {
    fun deleteById(id: ID)
    fun update(item: T): T?
    fun save(item: T): T
}