package io.github.denis1986.android.base.network.data

/**
 * Created by denis.druzhinin on 25.07.2019.
 */
open class NetworkResponse<T>: ThrowableContainer {
    var value: T? = null
    var code: Int? = null
    var isSuccessful: Boolean = true

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
        return "NetworkResponse(value=$value, code=$code, isSuccessful=$isSuccessful, throwable=$throwable)"
    }

    companion object {
        fun <T> success(value: T) = NetworkResponse<T>().apply { this.value = value }

        fun <T> error(aThrowable: Throwable, aValue: T? = null) = NetworkResponse<T>().apply {
            throwable = aThrowable
            value = aValue
        }

        fun <T> fromAnother(another: NetworkResponse<out Any>) = NetworkResponse<T>().apply {
            throwable = another.throwable
            code = another.code
            isSuccessful = another.isSuccessful
        }

        fun <T> fromExecutionResult(result: ExecutionResult<T>): NetworkResponse<T> {
            return NetworkResponse<T>().apply {
                throwable = result.throwable
                value = result.value
            }
        }

        fun <T> fromGeneralExecutionResult(result: ExecutionResult<out Any?>): NetworkResponse<T> {
            return NetworkResponse<T>().apply {
                throwable = result.throwable
            }
        }

        fun empty() = NetworkResponse<Unit>()
    }
}