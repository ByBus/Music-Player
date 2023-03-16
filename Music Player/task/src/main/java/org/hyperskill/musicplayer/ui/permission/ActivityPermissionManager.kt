package org.hyperskill.musicplayer.ui.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ActivityPermissionManager(private val activity: Activity) : PermissionManager {
    private val blocks = mutableMapOf<Int, () -> Unit>()
    private val permissions = mutableSetOf<Permission>()

    override fun protectWith(permission: Permission, block: () -> Unit) {
        if (checkPermission(permission)) {
            block()
        } else {
            blocks[permission.requestCode] = block
            permissions.add(permission)
            requestPermissions(permission)
        }
    }

    override fun handle(requestCode: Int): Boolean {
        val block = blocks[requestCode] ?: return false
        val permission = permissions.firstOrNull { it.requestCode == requestCode }!!
        val handled = checkPermission(permission)
        if (handled) {
            block()
            blocks.remove(requestCode)
            permissions.remove(permission)
        } else {
            Toast.makeText(activity, activity.getString(permission.rationale), Toast.LENGTH_SHORT)
                .show()
        }
        return handled
    }

    private fun checkPermission(permission: Permission): Boolean {
        return ContextCompat.checkSelfPermission(
                activity,
                permission.toString()
            ) == PackageManager.PERMISSION_GRANTED
    }
//
//    private fun shouldShowRequestPermissionRationale(permission: Permission): Boolean {
//        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.toString())
//    }

    private fun requestPermissions(permission: Permission) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission.toString()),
            permission.requestCode
        )
    }
}
