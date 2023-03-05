package org.hyperskill.musicplayer.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.hyperskill.musicplayer.R
import org.hyperskill.musicplayer.databinding.ListItemSongBinding
import org.hyperskill.musicplayer.databinding.ListItemSongSelectorBinding
import org.hyperskill.musicplayer.domain.SongMapper

class PlaylistAdapter(
    private val clickListener: ClickListener,
    private val durationMapper: SongMapper<String>
) : RecyclerView.Adapter<PlaylistAdapter.BindViewHolder>() {
    private val songItems = mutableListOf<SongUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SONG_LAYOUT -> {
                val binding = ListItemSongBinding.inflate(inflater, parent, false)
                SongViewHolder(binding)
            }
            SELECTOR_LAYOUT -> {
                val binding = ListItemSongSelectorBinding.inflate(inflater, parent, false)
                SelectorViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bind(songItems[position])
    }

    override fun getItemViewType(position: Int): Int {
        return songItems[position].layout()
    }

    fun update(uiState: UiState) {
        val diffCallback = SongDiffer(songItems, uiState.songs)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        songItems.clear()
        songItems.addAll(uiState.songs)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class SongViewHolder(binding: ListItemSongBinding) : BindViewHolder(binding.root) {
        private val artist: TextView = binding.songItemTvArtist
        private val title: TextView = binding.songItemTvTitle
        private val duration: TextView = binding.songItemTvDuration
        private val playButton: ImageButton = binding.songItemImgBtnPlayPause

        init {
            playButton.setOnClickListener {
                clickListener.onClick(id)
            }
        }

        override fun bind(item: SongUi) {
            id = item.id
            this.artist.text = item.artist
            this.title.text = item.title
            this.duration.text = item.map(durationMapper)
            this.playButton.setImageResource(if (item.state()) PAUSE_ICON else PLAY_ICON)
        }
    }

    inner class SelectorViewHolder(binding: ListItemSongSelectorBinding) :
        BindViewHolder(binding.root) {
        private val artist: TextView = binding.songSelectorItemTvArtist
        private val title: TextView = binding.songSelectorItemTvTitle
        private val duration: TextView = binding.songSelectorItemTvDuration
        private val checkBox: CheckBox = binding.songSelectorItemCheckBox

        init {
            itemView.setOnClickListener {
                clickListener.onClick(id)
            }
        }

        override fun bind(item: SongUi) {
            id = item.id
            this.artist.text = item.artist
            this.title.text = item.title
            this.duration.text = item.map(durationMapper)
            switch(item.state())
        }

        private fun switch(selected: Boolean) {
            checkBox.isChecked = selected
            itemView.setBackgroundColor(if (selected) SELECTED_COLOR else IDLE_COLOR)
        }
    }

    abstract inner class BindViewHolder(itemView: View) : ViewHolder(itemView) {
        protected var id = 0L

        init {
            itemView.setOnLongClickListener {
                clickListener.onLongClick(id)
                true
            }
        }

        abstract fun bind(item: SongUi)

    }

    interface ClickListener {
        fun onClick(id: Long)
        fun onLongClick(id: Long)
    }

    companion object {
        private const val SONG_LAYOUT = R.layout.list_item_song
        private const val SELECTOR_LAYOUT = R.layout.list_item_song_selector
        private const val PLAY_ICON = R.drawable.ic_play
        private const val PAUSE_ICON = R.drawable.ic_pause
        private const val SELECTED_COLOR = Color.LTGRAY
        private const val IDLE_COLOR = Color.WHITE
    }

    override fun getItemCount(): Int = songItems.size

}

class SongDiffer(
    private val oldList: List<SongUi>,
    private val newList: List<SongUi>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isSameAs(newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
