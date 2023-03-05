package org.hyperskill.musicplayer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.musicplayer.databinding.ActivityMainBinding
import org.hyperskill.musicplayer.domain.SongMapper
import org.hyperskill.musicplayer.ui.*

class MainActivity : AppCompatActivity() {
    private val uiStateToFragmentMapper = UiStateMapper.ToFragment()
    private var selectPlaylistDialog = ListDialog("", emptyList())
    private var deletePlaylistDialog = ListDialog("", emptyList())
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<SharedViewModel>() {
        SharedViewModelFactory((application as MediaPlayerApp).dependencyContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playlistAdapter = PlaylistAdapter(
            object : PlaylistAdapter.ClickListener {
                override fun onClick(id: Long) = viewModel.handleClick(id)

                override fun onLongClick(id: Long) = viewModel.handleLongClick(id)
            },
            SongMapper.DurationMapper()
        )
        binding.mainSongList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = playlistAdapter
            itemAnimator = null
        }

        viewModel.uiState.observe(this) {
            playlistAdapter.update(it)
            setBottomController(it)
            preparePlaylistDialogs(it)
        }

        viewModel.singleMessage.observe(this) {
            Toaster(it).show(this)
        }

        binding.mainButtonSearch.setOnClickListener {
            viewModel.newPlaylistOfAllSongs(getString(R.string.reserved_playlist_name))
        }
    }

    private fun preparePlaylistDialogs(uiState: UiState) {
        selectPlaylistDialog =
            ListDialog(getString(R.string.choose_playlist_to_load), uiState.playlists) {
                viewModel.loadSongsOfPlaylist(it)
            }
        deletePlaylistDialog = ListDialog(
            getString(R.string.choose_playlist_to_delete),
            uiState.playlists.filterNot { it.default }) {
            viewModel.deletePlaylist(it)
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
                    viewModel.showMessage(getString(R.string.no_songs_loaded))
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