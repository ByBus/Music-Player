package org.hyperskill.musicplayer.ui

import androidx.lifecycle.*
import org.hyperskill.musicplayer.domain.*
import org.hyperskill.musicplayer.sl.DependencyContainer

class SharedViewModel(
    private val handleItemClickUseCase: UseCase<Long, PlayerState>,
    private val loadAllSongsUseCase: UseCase<String, PlayerState>,
    private val deletePlaylistUseCase: UseCase<Playlist, PlayerState>,
    private val loadPlaylistSongsUseCase: UseCase<Playlist, PlayerState>,
    private val stopCurrentTrackUseCase: UnitUseCase<PlayerState>,
    private val changePlayerModeUseCase: CombinedUseCase<Long, PlayerState>,
    private val returnToPlayStateUseCase: UnitUseCase<PlayerState>,
    private val createPlaylistUseCase: UseCase<String, PlayerState>,
    cache: InMemoryCache<PlayerState>,
    uiMapper: PlayerStateMapper<UiState>
) : ViewModel() {
    private val playerState = MutableLiveData(cache.read())
    val uiState: LiveData<UiState> = playerState.map { it.map(uiMapper) }

    private val _singleMessage = SingleEvent<String>()
    val singleMessage: LiveData<String> = _singleMessage

    fun handleClick(id: Long) {
        playerState.value = handleItemClickUseCase(id)
    }

    fun playOrPauseCurrentSong() {
        handleClick(playerState.value?.currentTrack?.id ?: -1)
    }

    fun stopCurrentSong() {
        playerState.value = stopCurrentTrackUseCase()
    }

    fun handleLongClick(id: Long) {
        playerState.value = changePlayerModeUseCase(id)
    }

    fun selectionState() {
        playerState.value = changePlayerModeUseCase()
    }

    fun showMessage(message: String) {
        _singleMessage.value = message
    }

    fun createPlaylist(name: String) {
        playerState.value = createPlaylistUseCase(name)
    }

    fun isSongSelected() = playerState.value?.isAnySelected() ?: false
    fun loadSongsOfPlaylist(playlist: Playlist) {
        playerState.value = loadPlaylistSongsUseCase(playlist)
    }

    fun backToPlayState() {
        playerState.value = returnToPlayStateUseCase()
    }

    fun deletePlaylist(playlist: Playlist) {
        playerState.value = deletePlaylistUseCase(playlist)
    }

    fun newPlaylistOfAllSongs(title: String) {
        playerState.value = loadAllSongsUseCase(title)
    }
}

class SharedViewModelFactory(
    private val dependencyContainer: DependencyContainer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return dependencyContainer.module(modelClass).viewModel() as T
    }
}

