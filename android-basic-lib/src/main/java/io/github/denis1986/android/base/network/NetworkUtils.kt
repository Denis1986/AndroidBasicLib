package io.github.denis1986.android.base.network

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.reflect.KClass

/**
 * Created by denis.druzhinin on 03.11.2019.
 */
object NetworkUtils {
    fun isNoInternetException(throwable: Throwable?): Boolean = isExceptionOrContainerOfType(throwable, UnknownHostException::class)

    fun isTimeoutException(throwable: Throwable?): Boolean = isExceptionOrContainerOfType(throwable, SocketTimeoutException::class)

    fun isNetworkRelatedException(throwable: Throwable?) = isNoInternetException(throwable) || isTimeoutException(throwable)

    private fun isExceptionOrContainerOfType(throwable: Throwable?, classToCheck: KClass<out Any>): Boolean {
        return classToCheck.isInstance(throwable) || classToCheck.isInstance(throwable?.cause)
    }
}