package org.hyperskill.musicplayer.ui

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SkipFirstEvent<T> : MutableLiveData<T>() {
    private var isFirst = true
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { value ->
            if (isFirst.not()) {
                observer.onChanged(value)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        isFirst = false
        super.setValue(t)
    }
}