package org.hyperskill.musicplayer.player

import android.os.Handler
import android.os.Looper

interface Worker {
    fun newWorker(work: () -> Unit): Worker
    fun interrupt()
    fun start()

    class PeriodicHandler(private val period: Long, private val work: () -> Unit = {}) : Worker {
        @Volatile
        private var interrupted = false
        private val handler = Handler(Looper.getMainLooper())
        private val workCycle = {
            if (interrupted.not()) {
                start()
            }
        }

        override fun interrupt() {
            interrupted = true
            handler.removeCallbacks(workCycle)
        }

        override fun start() {
            work()
            handler.postDelayed(workCycle, period)
        }

        override fun newWorker(work: () -> Unit): Worker {
            return PeriodicHandler(period, work)
        }
    }
}
