package org.hyperskill.musicplayer.sl

import androidx.lifecycle.ViewModel
import org.hyperskill.musicplayer.ui.SharedViewModel

interface DependencyContainer {

    fun <T : ViewModel> module(clazz: Class<T>): Module<*>

    class Base() : DependencyContainer {
        override fun <T : ViewModel> module(clazz: Class<T>): Module<*> {
            return when (clazz) {
                SharedViewModel::class.java -> MainModule()
                else -> throw IllegalStateException("no module found for $clazz")
            }
        }
    }
}