package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.domain.Playlist
import org.hyperskill.musicplayer.domain.Repository
import org.hyperskill.musicplayer.domain.Song

class SongRepository(
    private val songDataSource: MultiIdsDataSource<SongDB, Long>,
    private val playlistDataSource: MutableDataSource<Playlist, String>,
    private val localPlaylistDataSource: DataSource<Playlist, Long>,
    private val mapper: SongDBMapper<Song>
) : Repository {
    private var needToInit = true

    override fun playlists() = localPlaylistDataSource.readAll() + playlistDataSource.readAll()

    override fun songsOfPlaylist(playlist: Playlist): List<Song> {
        if (needToInit && playlist.default.not()) {
            allSongs()
            needToInit = false
        }
        if (playlist.default) {
            needToInit = false
            return allSongs()
        }
        return songDataSource.findByIds(playlist.songsIds).map { it.map(mapper) }
    }

    override fun allSongs() = songDataSource.readAll().map { it.map(mapper) }

    override fun deletePlaylist(playlist: Playlist) {
        playlistDataSource.deleteById(playlist.title)
    }

    override fun createPlaylist(title: String, songsIds: List<Long>, default: Boolean): Playlist {
        if (default) return localPlaylistDataSource.findByName(title)!!

        val oldPlaylist = playlistDataSource.findByName(title)
        if (oldPlaylist != null) {
            val item = oldPlaylist.copy(songsIds = songsIds)
            return playlistDataSource.update(item) ?: item
        }
        val playlist = Playlist(0, title, songsIds)
        return playlistDataSource.save(playlist)
    }
}