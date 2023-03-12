package org.hyperskill.musicplayer.sl

import android.content.Context
import androidx.lifecycle.ViewModel
import org.hyperskill.musicplayer.ui.SharedViewModel

interface DependencyContainer {

    fun <T : ViewModel> module(clazz: Class<T>): Module<*>

    class Base(private val context: Context) : DependencyContainer {
        override fun <T : ViewModel> module(clazz: Class<T>): Module<*> {
            return when (clazz) {
                SharedViewModel::class.java -> MainModule(context)
                else -> throw IllegalStateException("no module found for $clazz")
            }
        }
    }
}