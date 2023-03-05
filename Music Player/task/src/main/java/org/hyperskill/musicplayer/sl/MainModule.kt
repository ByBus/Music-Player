package org.hyperskill.musicplayer.sl

import org.hyperskill.musicplayer.data.PlayerStateCache
import org.hyperskill.musicplayer.data.PlaylistDataSource
import org.hyperskill.musicplayer.data.SongDataSource
import org.hyperskill.musicplayer.data.SongRepository
import org.hyperskill.musicplayer.domain.PlayerStateMapper
import org.hyperskill.musicplayer.domain.usecase.*
import org.hyperskill.musicplayer.ui.SharedViewModel

class MainModule : Module<SharedViewModel> {
    private val provideRepository by lazy {
        SongRepository(SongDataSource(), PlaylistDataSource())
    }
    private val provideStateCache by lazy {
        PlayerStateCache()
    }
    private val provideMapper = PlayerStateMapper.ToUiState()

    override fun viewModel(): SharedViewModel {
        return SharedViewModel(
            HandleItemClickUseCase(
                PlayOrPauseTrackUseCase(provideStateCache),
                AddSongToSelectionUseCase(provideStateCache),
                provideStateCache
            ),
            LoadAllSongsUseCase(provideRepository, provideStateCache),
            DeletePlaylistUseCase(provideRepository, provideStateCache),
            LoadPlaylistSongsUseCase(provideRepository, provideStateCache),
            StopCurrentTrackUseCase(provideStateCache),
            ChangePlayerModeUseCase(provideStateCache),
            ReturnToPlayStateUseCase(provideRepository, provideStateCache),
            CreatePlaylistUseCase(provideRepository, provideStateCache),
            provideStateCache,
            provideMapper
        )
    }
}