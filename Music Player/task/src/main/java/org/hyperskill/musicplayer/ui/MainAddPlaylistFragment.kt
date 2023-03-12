package org.hyperskill.musicplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hyperskill.musicplayer.R
import org.hyperskill.musicplayer.databinding.FragmentMainAddPlaylistBinding

class MainAddPlaylistFragment : Fragment() {
    private val viewModel by activityViewModels<SharedViewModel>()
    private var _binding: FragmentMainAddPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPlaylistBtnCancel.setOnClickListener {
            viewModel.backToPlayState()
        }

        binding.addPlaylistBtnOk.setOnClickListener {
            val playlistName = binding.addPlaylistEtPlaylistName.text.toString()
            val messageId = when {
                viewModel.isSongSelected().not() -> R.string.add_atleast_one_song
                playlistName.isBlank() -> R.string.add_playlist_name
                playlistName == getString(R.string.reserved_playlist_name) ->
                    R.string.playlist_name_is_reserved
                else -> {
                    viewModel.createPlaylist(playlistName)
                    return@setOnClickListener
                }
            }
            viewModel.showMessage(getString(messageId))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}