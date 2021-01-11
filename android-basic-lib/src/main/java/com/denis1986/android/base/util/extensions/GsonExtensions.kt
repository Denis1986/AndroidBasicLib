package com.denis1986.android.base.util.extensions

import android.util.Log
import com.denis1986.android.base.service.LogsConsumer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Denis Druzhinin on 07.05.2020.
 */
object GsonExtensions {
    inline fun <reified T> Gson.fromJson(dataJson: String?) = if (dataJson == null) null else this.fromJson<T>(dataJson, object: TypeToken<T>() {}.type)

    inline fun <reified T> Gson.fromJsonSafe(dataJson: String?, logsConsumer: LogsConsumer?): T? {
        return try {
            fromJson<T>(dataJson)
        } catch (e: Exception) {
            logException(e, "Failed to restore type ${T::class.simpleName} from json", logsConsumer)
            null
        }
    }

    inline fun <T> Gson.fromJsonSafe(dataJson: String?, type: Type, logsConsumer: LogsConsumer?): T? {
        return try {
            fromJson(dataJson, type)
        } catch (e: Exception) {
            logException(e, "Failed to restore type $type from json", logsConsumer)
            null
        }
    }

    fun logException(exception: Exception, message: String, logsConsumer: LogsConsumer?) {
        if (logsConsumer == null) {
            Log.e(TAG, message, exception)
        } else {
            logsConsumer.e(TAG, message)
        }
    }

    val TAG = GsonExtensions::class.simpleName!!
}