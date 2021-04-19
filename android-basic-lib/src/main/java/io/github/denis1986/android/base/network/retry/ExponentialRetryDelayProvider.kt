package io.github.denis1986.android.base.network.retry

import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.random.Random

/**
 * Created by Denis Druzhinin on 02.05.2020.
 */
class ExponentialRetryDelayProvider(maxAttempts: Int,
                                    private val minimalDelayMs: Long): RetryDelayProvider(maxAttempts) {

    /** @return minimalDelayMs * Random[2^(i-1) - 1; 2^i - 1]
     */
    override fun calculate(attempt: Int): Long {
        val previous = 2.0.pow(attempt - 1) - 1
        val current = 2.0.pow(attempt)
        return minimalDelayMs * Random.nextLong(from = previous.roundToLong(), until = current.roundToLong())
    }
}