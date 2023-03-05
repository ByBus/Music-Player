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
    private val cache: InMemoryCache<PlayerState>,
    private val uiMapper: PlayerStateMapper<UiState>
) : ViewModel() {
    private val _uiState = MutableLiveData(cache.read().map(uiMapper))
    val uiState: LiveData<UiState> = _uiState

    private val _singleMessage = SingleEvent<String>()
    val singleMessage: LiveData<String> = _singleMessage

    fun handleClick(id: Long) = updateUiState { handleItemClickUseCase(id) }

    fun playOrPauseCurrentSong() = handleClick(cache.read().currentTrack.id)

    fun stopCurrentSong() = updateUiState { stopCurrentTrackUseCase() }

    fun handleLongClick(id: Long) = updateUiState { changePlayerModeUseCase(id) }

    fun selectionState() = updateUiState { changePlayerModeUseCase() }

    fun showMessage(message: String) {
        _singleMessage.value = message
    }

    fun createPlaylist(name: String) = updateUiState { createPlaylistUseCase(name) }

    fun isSongSelected() = cache.read().isAnySelected()

    fun loadSongsOfPlaylist(playlist: Playlist) =
        updateUiState { loadPlaylistSongsUseCase(playlist) }

    fun backToPlayState() = updateUiState { returnToPlayStateUseCase() }

    fun deletePlaylist(playlist: Playlist) = updateUiState { deletePlaylistUseCase(playlist) }

    fun newPlaylistOfAllSongs(title: String) = updateUiState { loadAllSongsUseCase(title) }

    private fun updateUiState(stateProducer: () -> PlayerState) {
        _uiState.value = stateProducer().map(uiMapper)
    }
}

class SharedViewModelFactory(
    private val dependencyContainer: DependencyContainer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return dependencyContainer.module(modelClass).viewModel() as T
    }
}