package com.denis1986.android.base.sample.base

import com.denis1986.android.base.network.retry.ExponentialRetryDelayProvider
import com.denis1986.android.base.network.retry.RetryDelayProvider

/**
 * Created by Denis Druzhinin on 02.05.2020.
 */
class RetryPolicy {

    fun getBackgroundOperationRetryDelayProvider(): RetryDelayProvider = ExponentialRetryDelayProvider(
        BACKGROUND_OPERATION_MAX_ATTEMPTS, BACKGROUND_OPERATION_MINIMAL_DELAY_MS
    )

    companion object {
        const val BACKGROUND_OPERATION_MAX_ATTEMPTS = 10
        const val BACKGROUND_OPERATION_MINIMAL_DELAY_MS: Long = 500
    }
}