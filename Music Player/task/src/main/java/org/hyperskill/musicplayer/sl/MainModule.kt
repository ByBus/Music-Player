package org.hyperskill.musicplayer.sl

import android.content.Context
import org.hyperskill.musicplayer.data.*
import org.hyperskill.musicplayer.ui.Communication
import org.hyperskill.musicplayer.domain.PlaybackState
import org.hyperskill.musicplayer.domain.PlayerStateMapper
import org.hyperskill.musicplayer.domain.usecase.*
import org.hyperskill.musicplayer.player.AudioPlayer
import org.hyperskill.musicplayer.player.PeriodicHandler
import org.hyperskill.musicplayer.ui.mapper.PlaybackMapper
import org.hyperskill.musicplayer.ui.SharedViewModel
import org.hyperskill.musicplayer.ui.mapper.StringFormatter

class MainModule(context: Context) : Module<SharedViewModel> {
    private val provideRepository by lazy {
        SongRepository(TestSongDataSource(TestAudioFileProvider(context, "wisdom.mp3")), PlaylistDataSource())
    }
    private val provideStateCache by lazy {
        PlayerStateCache()
    }

    private val providePlaybackCommunication by lazy { Communication.Base<PlaybackState>() }
    private val providePlayer by lazy { AudioPlayer(context, providePlaybackCommunication, PeriodicHandler(100)) }
    private val provideUiStateMapper = PlayerStateMapper.ToUiState()
    private val providePlaybackUiMapper = PlaybackMapper.ToUi(StringFormatter.MillisToTime())

    override fun viewModel(): SharedViewModel {
        return SharedViewModel(
            HandleItemClickUseCase(
                PlayOrPauseTrackUseCase(provideStateCache, providePlayer),
                AddSongToSelectionUseCase(provideStateCache),
                provideStateCache
            ),
            LoadAllSongsUseCase(provideRepository, provideStateCache, providePlayer),
            DeletePlaylistUseCase(provideRepository, provideStateCache),
            LoadPlaylistSongsUseCase(provideRepository, provideStateCache, providePlayer),
            StopCurrentTrackUseCase(provideStateCache, providePlayer),
            ChangePlayerModeUseCase(provideStateCache),
            ReturnToPlayStateUseCase(provideRepository, provideStateCache),
            SetSongProgressUseCase(providePlayer),
            CreatePlaylistUseCase(provideRepository, provideStateCache),
            provideStateCache,
            provideUiStateMapper,
            providePlaybackUiMapper,
            providePlaybackCommunication
        )
    }
}