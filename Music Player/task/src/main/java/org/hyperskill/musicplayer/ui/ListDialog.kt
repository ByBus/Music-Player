package org.hyperskill.musicplayer.ui

import android.app.AlertDialog
import android.content.Context
import org.hyperskill.musicplayer.R
import org.hyperskill.musicplayer.domain.Playlist

class ListDialog(
    private val title: String,
    private val items: List<Playlist>,
    private val onConfirm: (Playlist) -> Unit = {}
) {
    fun show(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setItems(items.map { it.title }.toTypedArray()) { _, i ->
                onConfirm(items[i])
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    fun copy(items: List<Playlist>, onConfirm: (Playlist) -> Unit = {}) : ListDialog {
        return ListDialog(title, items, onConfirm)
    }
}