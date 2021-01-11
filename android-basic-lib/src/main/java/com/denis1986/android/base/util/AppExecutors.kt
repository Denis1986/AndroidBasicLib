package com.denis1986.android.base.util

import android.os.Looper
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class AppExecutors(
        private val diskIO: Executor,
        private val networkIO: Executor,
        private val mainThread: Executor
) {
    val diskIODispatcher = diskIO.asCoroutineDispatcher()
    val networkIODispatcher = networkIO.asCoroutineDispatcher()
    val mainThreadDispatcher = mainThread.asCoroutineDispatcher()

    class MainThreadExecutor : Executor {
        private val mainThreadHandler = android.os.Handler(Looper.getMainLooper())

        override fun execute(p0: Runnable) {
            mainThreadHandler.post(p0)
        }
    }

    constructor() : this(
            Executors.newSingleThreadExecutor(),
            Executors.newFixedThreadPool(THREAD_COUNT),
            MainThreadExecutor()
    )


    fun diskIO() = diskIO

    fun networkIO() = networkIO

    fun mainThread() = mainThread


    companion object {
        private const val THREAD_COUNT = 1
    }
}