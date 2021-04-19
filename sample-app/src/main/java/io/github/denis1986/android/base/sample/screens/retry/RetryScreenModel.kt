package io.github.denis1986.android.base.sample.screens.retry

import android.app.Application
import androidx.lifecycle.viewModelScope
import io.github.denis1986.android.base.data.SynchronizedValue
import io.github.denis1986.android.base.mvvm.MediatorLiveDataWrapper
import io.github.denis1986.android.base.sample.base.SampleBasicViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Denis Druzhinin on 08.01.2021.
 */
class RetryScreenModel(app: Application) : SampleBasicViewModel(app) {

    private val networkStateManager = getAppComponent().provideNetworkStateManager()
    private val executors = getAppComponent().provideAppExecutors()
    private val retryPolicy = getAppComponent().provideRetryPolicy()

    private var lastRetryableExecutor = SynchronizedValue<SampleRetryableExecutor>()
    @Volatile
    private var isWaitingToRetryRequest: Boolean = false
    private var lastRetryParam: RetryParam? = null

    val log = MediatorLiveDataWrapper<String>()

    init {
        log.asMediator().addSource(networkStateManager.networkState.get()) { connectionState ->
            if (!connectionState.hasConnection)
                return@addSource

            if (isWaitingToRetryRequest) {
                addLog("Network connection is available again. Restarting request.")
                restartRequest()
            }
        }
    }

    private fun restartRequest() {
        lastRetryParam?.let { startRetryableExecutor(it) }
    }

    fun onSendRequestButtonClicked(retryParam: RetryParam) {
        log.asMutable().value = ""
        lastRetryParam = retryParam
        isWaitingToRetryRequest = false
        startRetryableExecutor(retryParam)
    }

    fun onCancelButtonClicked() {
        isWaitingToRetryRequest = false
        lastRetryableExecutor.get()?.cancel()
        lastRetryableExecutor.set(null)
        addLog("Cancelling current request.")
    }

    private fun startRetryableExecutor(retryParam: RetryParam) {
        if (lastRetryableExecutor.get() != null) {
            addLog("Retryable executor is already started.")
        } else {
            val newExecutor = SampleRetryableExecutor(executors, retryPolicy, retryParam, object: SampleRetryableExecutor.Listener {
                override suspend fun onSuccess() {
                    isWaitingToRetryRequest = false
                    lastRetryableExecutor.set(null)
                    withContext(executors.mainThreadDispatcher) { addLog("The request was executed successfully.") }
                }

                override suspend fun onNetworkException() {
                    isWaitingToRetryRequest = true
                    lastRetryableExecutor.set(null)
                    withContext(executors.mainThreadDispatcher) { addLog("No network connection.") }
                }

                override fun onNewAttemptStarted(attempt: Int) {
                    viewModelScope.launch {
                        addLog("Attempt number $attempt is started")
                    }
                }
            })
            lastRetryableExecutor.set(newExecutor)
            newExecutor.execute()
        }
    }

    private fun addLog(newLog: String) {
        val currentLog = log.get().value
        log.asMediator().value = if (currentLog == null) newLog else "$currentLog\n$newLog"
    }
}