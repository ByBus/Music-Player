package org.hyperskill.musicplayer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.musicplayer.databinding.ActivityMainBinding
import org.hyperskill.musicplayer.ui.*
import org.hyperskill.musicplayer.ui.mapper.SongMapper
import org.hyperskill.musicplayer.ui.mapper.StringFormatter
import org.hyperskill.musicplayer.ui.mapper.UiStateMapper
import org.hyperskill.musicplayer.ui.permission.Permission.ReadExternalStorageForSongs
import org.hyperskill.musicplayer.ui.permission.ActivityPermissionManager

class MainActivity : AppCompatActivity() {
    private val uiStateToFragmentMapper = UiStateMapper.ToFragment()
    private lateinit var selectPlaylistDialog: ListDialog
    private lateinit var deletePlaylistDialog: ListDialog
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<SharedViewModel>() {
        SharedViewModelFactory((application as MediaPlayerApp).dependencyContainer)
    }

    private val permissionManager = ActivityPermissionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectPlaylistDialog = ListDialog(getString(R.string.choose_playlist_to_load), emptyList()) {
            viewModel.loadSongsOfPlaylist(it)
        }

        deletePlaylistDialog = ListDialog(getString(R.string.choose_playlist_to_delete), emptyList()) {
            viewModel.deletePlaylist(it)
        }

        val playlistAdapter = PlaylistAdapter(
            object : PlaylistAdapter.ClickListener {
                override fun onClick(id: Long) = viewModel.handleClick(id)
                override fun onLongClick(id: Long) = viewModel.handleLongClick(id)
            },
            SongMapper.DurationMapper(StringFormatter.MillisToTime())
        )

        binding.mainSongList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = playlistAdapter
//            itemAnimator = null
        }

        viewModel.uiState.observe(this) {
            it.checkForWarning(viewModel, getString(R.string.no_songs_found))
            playlistAdapter.update(it)
            setBottomController(it)
            selectPlaylistDialog.items(it.playlists)
            deletePlaylistDialog.items(it.userPlaylists)
        }

        viewModel.playbackUiState.observe(this) {
            it.complete(viewModel)
        }

        viewModel.singleMessage.observe(this) {
            Toaster(it).show(this)
        }

        binding.mainButtonSearch.setOnClickListener {
            permissionManager.protectWith(ReadExternalStorageForSongs) {
                viewModel.newPlaylistOfAllSongs(getString(R.string.reserved_playlist_name))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.playlist_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mainMenuAddPlaylist -> {
                if (binding.mainSongList.childCount == 0) {
                    viewModel.handleMessage(getString(R.string.no_songs_loaded))
                } else {
                    viewModel.selectionState()
                }
                true
            }
            R.id.mainMenuLoadPlaylist -> {
                selectPlaylistDialog.show(this)
                true
            }
            R.id.mainMenuDeletePlaylist -> {
                deletePlaylistDialog.show(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val isHandled = permissionManager.handle(requestCode)
        if (isHandled.not()) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun setBottomController(uiState: UiState) {
        val fragmentClass = uiState.map(uiStateToFragmentMapper)
        val tag = fragmentClass.simpleName
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFragmentContainer, fragmentClass.newInstance(), tag)
                .commit()
        }
    }
}