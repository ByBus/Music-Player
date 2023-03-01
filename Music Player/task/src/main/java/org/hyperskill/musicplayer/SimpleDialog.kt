package org.hyperskill.musicplayer

import android.app.AlertDialog
import android.content.Context

class SimpleDialog(private val title: String, private val message: String = "") {
    fun show(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}