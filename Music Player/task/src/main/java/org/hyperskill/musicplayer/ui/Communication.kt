package org.hyperskill.musicplayer.ui

import androidx.lifecycle.*

interface Communication {

    interface Observe<T> {
        fun observe(owner: LifecycleOwner, observer: Observer<T>)
        fun <T2> map(transform: (T) -> T2): LiveData<T2>
    }

    interface Update<T> {
        fun update(value: T)
        fun postUpdate(value: T)
    }

    interface Mutable<T> : Observe<T>, Update<T>

    class Base<T>(
        private val liveData: MutableLiveData<T> = MutableLiveData()
    ) : Mutable<T> {
        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }

        override fun update(value: T) {
            liveData.value = value
        }

        override fun postUpdate(value: T) {
            liveData.postValue(value)
        }

        override fun <T2> map(transform: (T) -> T2): LiveData<T2> {
            return liveData.map(transform)
        }
    }
}
