package com.denis1986.android.base.sample.utils

/**
 * Created by Denis Druzhinin on 07.01.2021.
 */
object CommonExtensions {

    fun String?.nullToEmpty(): String = this ?: ""

    fun Boolean?.nullToFalse(): Boolean = this ?: false
}