package io.github.denis1986.android.base.network.retry

/** Basic class for retry delay providers.
 *
 * Created by Denis Druzhinin on 02.05.2020.
 */
abstract class RetryDelayProvider(private val maxAttempts: Int) {

    /**
     * @return null if number of attempts reaches [maxAttempts], otherwise time period in milliseconds.
     */
    fun next(attempt: Int = START_ATTEMPT): Long? {
        if (attempt < START_ATTEMPT)
            return 0

        return if (attempt > maxAttempts) null else calculate(attempt)
    }

    abstract fun calculate(attempt: Int): Long

    companion object {
        const val START_ATTEMPT = 1
    }
}