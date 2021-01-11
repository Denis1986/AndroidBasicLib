package com.denis1986.android.base.network.data

/**
 * Created by denis.druzhinin on 11.10.2019.
 */
open class ExecutionResult<T> protected constructor(
        @Volatile override var throwable: Throwable? = null,
        @Volatile var value: T? = null
): ThrowableContainer {

    var isSuccessfulAdditional: Boolean = true

    fun isSuccessful(): Boolean = (throwable == null) && isSuccessfulAdditional

    companion object {
        fun <T> success(value: T) = ExecutionResult(value = value)

        fun <T> error(throwable: Throwable?) = ExecutionResult<T>(throwable = throwable).apply { isSuccessfulAdditional = false }

        fun empty(throwable: Throwable? = null) = ExecutionResult<Unit>(throwable = throwable)
    }
}