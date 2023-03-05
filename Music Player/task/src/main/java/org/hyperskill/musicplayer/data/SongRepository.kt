package org.hyperskill.musicplayer.data

import org.hyperskill.musicplayer.domain.Playlist
import org.hyperskill.musicplayer.domain.Repository
import org.hyperskill.musicplayer.domain.Song

class SongRepository(
    private val songDataSource: SongDataSource,
    private val playlistDataSource: PlaylistDataSource
) : Repository {
    private var id = 1L
    override fun playlists() = playlistDataSource.readAll()

    override fun songsOfPlaylist(playlist: Playlist) = songDataSource.findByIds(playlist.songsIds)

    override fun allSongs() = songDataSource.readAll()

    override fun deletePlaylist(playlist: Playlist) {
        playlistDataSource.deleteById(playlist.id)
    }

    override fun addSongsToPlaylist(playlist: Playlist, songs: List<Song>) {
        playlistDataSource.findById(playlist.id)?.let {
            val songsIds = (it.songsIds + songs.map { s -> s.id }).distinct()
            playlistDataSource.update(it.id, songsIds)
        }
    }

    override fun createPlaylist(title: String, songsIds: List<Long>, default: Boolean): Playlist {
        val oldPlaylist = playlistDataSource.findByName(title)
        if (oldPlaylist != null) {
            return playlistDataSource.update(oldPlaylist.id, songsIds)  ?: oldPlaylist
        }
        val playlist = Playlist(id++, title, songsIds, default)
        return playlistDataSource.create(playlist)
    }
}