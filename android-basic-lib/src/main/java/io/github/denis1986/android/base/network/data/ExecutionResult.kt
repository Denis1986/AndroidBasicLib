package io.github.denis1986.android.base.network.data

/**
 * Created by denis.druzhinin on 11.10.2019.
 */
open class ExecutionResult<T> protected constructor(
        @Volatile var value: T? = null
): ThrowableContainer {

    @Volatile
    var isSuccessful: Boolean = true

    @Volatile
    override var throwable: Throwable? = null
        set(value) {
            if (value != null) {
                isSuccessful = false
            }
            field = value
        }

    fun isFilled(): Boolean {
        return (isSuccessful && value != null)
    }

    override fun toString(): String {
        return "ExecutionResult(value=$value, isSuccessful=$isSuccessful, throwable=$throwable)"
    }

    companion object {
        fun <T> success(value: T) = ExecutionResult<T>().apply {
            this.value = value
        }

        fun <T> error(throwable: Throwable?) = ExecutionResult<T>().apply {
            this.throwable = throwable
        }

        fun empty(throwable: Throwable? = null) = ExecutionResult<Unit>().apply {
            this.throwable = throwable
        }

        fun <T> fromAnother(another: ExecutionResult<*>) = ExecutionResult<T>().apply {
            throwable = another.throwable
            isSuccessful = another.isSuccessful
        }
    }
}