package com.denis1986.android.base.sample.screens.retry

import com.denis1986.android.base.network.NetworkUtils
import com.denis1986.android.base.network.retry.RetryableExecutor
import com.denis1986.android.base.sample.SampleApplication
import com.denis1986.android.base.sample.base.RetryPolicy
import com.denis1986.android.base.util.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Denis Druzhinin on 08.01.2021.
 */
class SampleRetryableExecutor(private val executors: AppExecutors,
                              retryPolicy: RetryPolicy,
                              retryParam: RetryParam,
                              private val listener: Listener
) : RetryableExecutor(SampleApplication.instance.appComponent.provideAppExecutors().networkIODispatcher, retryPolicy.getBackgroundOperationRetryDelayProvider()) {

    interface Listener : RetryableExecutor.Listener {
        suspend fun onSuccess()
        fun onNewAttemptStarted(attempt: Int)
    }

    private val dataSource = FakeDataSource(retryParam)

    override fun startExecution(attempt: Int) {
        listener.onNewAttemptStarted(attempt)

        CoroutineScope(executors.diskIODispatcher).launch {
            val response = dataSource.interactWithBackend()
            if (response.isSuccessful) {
                listener.onSuccess()
            } else { // Some error occurred.
                if (NetworkUtils.isNetworkRelatedException(response.throwable)) {
                    listener.onNetworkException()
                } else { // Error is not related to network problems.
                    retry(attempt)
                }
            }
        }
    }
}