package org.hyperskill.musicplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hyperskill.musicplayer.databinding.FragmentMainPlayerControllerBinding

class MainPlayerControllerFragment : Fragment() {
    private val viewModel by activityViewModels<SharedViewModel>()
    private var _binding: FragmentMainPlayerControllerBinding? = null
    private val binding get() = _binding!!
    private var isChangingProgress = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPlayerControllerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.playbackUiState.observe(viewLifecycleOwner) { playback ->
            binding.controllerTvCurrentTime.text = playback.currentTime
            binding.controllerTvTotalTime.text = playback.durationTime
            binding.controllerSeekBar.max = playback.max
            if (isChangingProgress.not()) {
                binding.controllerSeekBar.progress = playback.current
            }
        }

        binding.controllerBtnPlayPause.setOnClickListener {
            viewModel.playOrPauseCurrentSong()
        }

        binding.controllerBtnStop.setOnClickListener {
            viewModel.stopCurrentSong()
        }

        binding.controllerSeekBar.setOnSeekBarChangeListener(object : SeekBarListener() {
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isChangingProgress = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isChangingProgress = false
                viewModel.seekCurrentSong(seekBar.progress)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}