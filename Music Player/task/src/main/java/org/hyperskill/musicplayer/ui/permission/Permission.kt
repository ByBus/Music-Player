package org.hyperskill.musicplayer.ui.permission

import android.Manifest
import androidx.annotation.StringRes
import org.hyperskill.musicplayer.R

sealed class Permission(
    private val permission: String,
    val requestCode: Int,
    @StringRes val rationale: Int
) {
    override fun toString() = permission

    object ReadExternalStorageForSongs : Permission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        1,
        R.string.songs_load_permission_rationale
    )
}