package org.hyperskill.musicplayer.player

import android.os.Handler
import android.os.Looper

abstract class Worker : Thread() {
    abstract fun newWorker(work: () -> Unit): Worker
}

// Not working in tests
//class PeriodicWorker(private val period: Long, private val work: () -> Unit = {}) : Worker() {
//    override fun run() {
//        super.run()
//        while (!currentThread().isInterrupted) {
//            try {
//                sleep(period)
//            } catch (_: InterruptedException) {
//                currentThread().interrupt()
//            }
//            if (!currentThread().isInterrupted) {
//                try {
//                    work()
//                } catch (e: IllegalStateException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    override fun newWorker(work: () -> Unit): PeriodicWorker {
//        return PeriodicWorker(period, work)
//    }
//}

class PeriodicHandler(private val period: Long, private val work: () -> Unit = {}) : Worker() {
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