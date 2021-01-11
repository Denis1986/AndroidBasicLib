package com.denis1986.android.base.service

/**
 * Created by denis.druzhinin on 05.11.2019.
 */
interface LogsConsumer {
    fun v(tag: String, msg: String)
    fun d(tag: String, msg: String)
    fun i(tag: String, msg: String)
    fun w(tag: String, msg: String, throwable: Throwable? = null)
    fun e(tag: String, msg: String, throwable: Throwable? = null)
}