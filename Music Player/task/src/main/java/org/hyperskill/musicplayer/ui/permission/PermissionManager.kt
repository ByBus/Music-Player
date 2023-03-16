package org.hyperskill.musicplayer.ui.permission

interface PermissionManager {
    fun protectWith(permission: Permission, block: () -> Unit)
    fun handle(requestCode: Int): Boolean
}