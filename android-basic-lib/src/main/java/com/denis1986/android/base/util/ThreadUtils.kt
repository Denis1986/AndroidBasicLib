package com.denis1986.android.base.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Future

/**
 * Created by denis.druzhinin on 21.06.2019.
 */
class ThreadUtils private constructor() {
    companion object {
        @JvmStatic
        fun getInstance() : ThreadUtils {
            return ThreadUtils()
        }
    }

    private var sHandler: Handler? = null

    fun getUiThreadHandler(): Handler? {
        initializeHandlerIfNecessary()
        return sHandler
    }

    /**
     * Adds given runnable to the UI thread message queue even if current thread is UI thread.
     * @param runnable
     */
    @Synchronized
    fun postToUiThread(runnable: Runnable) {
        initializeHandlerIfNecessary()
        sHandler!!.post(runnable)
    }

    @Synchronized
    fun postDelayedToUiThread(runnable: Runnable, delayMillis: Long) {
        initializeHandlerIfNecessary()
        sHandler!!.postDelayed(runnable, delayMillis)
    }

    /**
     * If current thread is UI thread, given runnable is executed immediately. Otherwise `postToUiThread()` method is called.
     * @param runnable
     */
    fun runOnUiThread(runnable: Runnable) {
        if (isUiThread()) {
            runnable.run()
        } else
            postToUiThread(runnable)
    }

    fun isUiThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }

    fun <T> getValue(future: Future<T>): T? {
        return try {
            future.get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private fun initializeHandlerIfNecessary() {
        if (sHandler == null) {
            sHandler = Handler(Looper.getMainLooper())
        }
    }

    interface ThrowingRunnable {
        @Throws(Exception::class)
        fun run()
    }
}