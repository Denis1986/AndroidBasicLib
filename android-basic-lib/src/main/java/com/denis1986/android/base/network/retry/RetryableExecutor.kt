package com.denis1986.android.base.network.retry

import kotlinx.coroutines.*

/** Executor, which implements retry mechanism. This mechanism is invoked from derived classes.
 *
 * Usually retry mechanism should not be used, if network related exception occurred.
 * Consider listening for [com.denis1986.android.base.network.NetworkStateManager]'s connectionState instead.
 *
 * Created by Denis Druzhinin on 31.08.2020.
 */
abstract class RetryableExecutor(
    private val dispatcher: CoroutineDispatcher,
    private val retryPeriodProvider: RetryDelayProvider) {

    interface Listener {
        suspend fun onNetworkException()
    }


    @Volatile
    private var isExecutionStarted = false

    @Volatile
    var isCanceled = false
        private set

    @Volatile
    private var retryJob: Job? = null


    fun execute() {
        if (isExecutionStarted)
            return

        startExecution(0)
        isExecutionStarted = true
    }

    fun cancel() {
        isCanceled = true
        retryJob?.cancel()
    }

    protected abstract fun startExecution(attempt: Int)

    protected open fun retry(attempt: Int) {
        val delayMs = retryPeriodProvider.next(attempt)
        if (delayMs == null || isCanceled) { // Retry attempts exhausted.
            retryJob = null
            return
        }

        retryJob = CoroutineScope(dispatcher).launch {
            delay(delayMs)

            if (isActive) {
                startExecution(attempt + 1)
            }
        }
    }
}