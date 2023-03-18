package org.hyperskill.musicplayer.sl

import android.content.Context
import androidx.room.Room
import org.hyperskill.musicplayer.data.*
import org.hyperskill.musicplayer.data.room.AppDataBase
import org.hyperskill.musicplayer.data.room.PlaylistDbLocalDataSource
import org.hyperskill.musicplayer.domain.Finalizer
import org.hyperskill.musicplayer.domain.PlaybackState
import org.hyperskill.musicplayer.domain.PlayerStateMapper
import org.hyperskill.musicplayer.domain.usecase.*
import org.hyperskill.musicplayer.player.AudioPlayer
import org.hyperskill.musicplayer.player.Worker
import org.hyperskill.musicplayer.ui.Communication
import org.hyperskill.musicplayer.ui.SharedViewModel
import org.hyperskill.musicplayer.ui.mapper.PlaybackMapper
import org.hyperskill.musicplayer.ui.mapper.StringFormatter

class MainModule(context: Context) : Module<SharedViewModel> {
    private val provideRoom by lazy {
        Room.databaseBuilder(
            context,
            AppDataBase::class.java, "musicPlayerDatabase.db"
        ).allowMainThreadQueries().build()
    }

    private val provideRepository by lazy {
        SongRepository(
            LocalSongDataSource(context),
            PlaylistDbLocalDataSource(
                provideRoom.dao()
            ),
            InMemoryDefaultPlayListDataSource(),
            SongDBMapper.ToDomain()
        )
    }
    private val provideStateCache by lazy {
        PlayerStateCache(provideRepository)
    }

    private val providePlaybackCommunication by lazy { Communication.Base<PlaybackState>() }
    private val providePlayer by lazy {
        AudioPlayer(
            context,
            providePlaybackCommunication,
            Worker.PeriodicHandler(100)
        )
    }
    private val provideUiStateMapper = PlayerStateMapper.ToUi()
    private val providePlaybackUiMapper = PlaybackMapper.ToUi(StringFormatter.MillisToTime())

    override fun viewModel(): SharedViewModel {
        val loadPlaylistSongsUseCase =
            LoadPlaylistSongsUseCase(provideRepository, provideStateCache, providePlayer)
        return SharedViewModel(
            HandleItemClickUseCase(
                PlayOrPauseTrackUseCase(provideStateCache, providePlayer),
                AddSongToSelectionUseCase(provideStateCache),
                provideStateCache
            ),
            FindAllSongsInStorageUseCase(provideRepository, loadPlaylistSongsUseCase),
            DeletePlaylistUseCase(provideRepository, provideStateCache),
            loadPlaylistSongsUseCase,
            StopCurrentTrackUseCase(provideStateCache, providePlayer),
            ChangePlayerModeUseCase(provideStateCache),
            ReturnToPlayStateUseCase(provideRepository, provideStateCache),
            SetSongProgressUseCase(providePlayer),
            CreatePlaylistUseCase(provideRepository, provideStateCache),
            provideStateCache,
            provideUiStateMapper,
            providePlaybackUiMapper,
            providePlaybackCommunication,
            Finalizer.ResourceReleaser(providePlayer)
        )
    }
}