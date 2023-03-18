package org.hyperskill.musicplayer.domain


data class PlayerState(
    var currentTrack: Song,
    var mode: Mode,
    var currentPlaylist: Playlist,
    var playlists: List<Playlist>,
    var selectedSongs: MutableSet<Long>,
    var allSongs: List<Song>,
    val isInitial: Boolean,
) {
    fun <T> map(mapper: PlayerStateMapper<T>): T {
        return mapper(
            currentTrack,
            mode,
            currentPlaylist,
            playlists,
            selectedSongs,
            allSongs,
            isInitial
        )
    }

    fun updateSelection(selection: Iterable<Long> = selectedSongs) {
        this.selectedSongs = allSongs.map { it.id }.intersect(selection.toSet()).toMutableSet()
    }

    fun isAnySelected() = selectedSongs.isNotEmpty()
}