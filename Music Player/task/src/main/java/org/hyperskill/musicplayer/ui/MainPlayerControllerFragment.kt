package org.hyperskill.musicplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hyperskill.musicplayer.databinding.FragmentMainPlayerControllerBinding

class MainPlayerControllerFragment : Fragment() {
    private val viewModel by activityViewModels<SharedViewModel>()
    private var _binding: FragmentMainPlayerControllerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPlayerControllerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.controllerBtnPlayPause.setOnClickListener {
            viewModel.playOrPauseCurrentSong()
        }

        binding.controllerBtnStop.setOnClickListener {
            viewModel.stopCurrentSong()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}