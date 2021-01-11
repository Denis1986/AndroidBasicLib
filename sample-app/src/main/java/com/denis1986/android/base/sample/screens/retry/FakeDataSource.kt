package com.denis1986.android.base.sample.screens.retry

import com.denis1986.android.base.network.data.NetworkResponse
import com.denis1986.android.base.sample.SampleApplication
import com.denis1986.android.base.sample.utils.CommonExtensions.nullToFalse
import java.lang.Exception
import java.net.UnknownHostException

/**
 * Created by Denis Druzhinin on 08.01.2021.
 */
class FakeDataSource(val retryParam: RetryParam) {

    private var currentAttemptCount = 0

    private val connectionManager = SampleApplication.instance.appComponent.provideConnectionManager()

    fun interactWithBackend(): NetworkResponse<Unit> {
        if (retryParam.takeIntoAccountNetworkState && !connectionManager.connectionState.get().value?.hasConnection.nullToFalse())
            return NetworkResponse.error(UnknownHostException())

        currentAttemptCount++
        return if (currentAttemptCount <= retryParam.failedAttemptCount)
            NetworkResponse.error(Exception())
        else NetworkResponse.empty()
    }
}