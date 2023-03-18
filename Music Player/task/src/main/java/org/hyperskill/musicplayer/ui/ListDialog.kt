package org.hyperskill.musicplayer.ui

import android.app.AlertDialog
import android.content.Context
import org.hyperskill.musicplayer.R
import org.hyperskill.musicplayer.domain.Playlist

class ListDialog(
    private val title: String,
    private var items: List<Playlist>,
    private val onConfirm: (Playlist) -> Unit
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

    fun items(newItems: List<Playlist>) {
        items = newItems.toList()
    }
}