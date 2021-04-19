package io.github.denis1986.android.base.io

import com.google.gson.Gson
import io.github.denis1986.android.base.service.LogsConsumer
import io.github.denis1986.android.base.util.extensions.GsonExtensions.fromJsonSafe
import java.lang.reflect.Type

/** Gives an ability to save complex data structures to preferences.
 *
 * Created by Denis Druzhinin on 06.07.2020.
 */
class PreferencesObjectSaver<T>(private val key: String,
                                private val type: Type,
                                private val preferences: BasicPreferences,
                                private val gson: Gson,
                                private val logsConsumer: LogsConsumer? = null
) : Saver<T> {

    override fun save(value: T?) {
        preferences.putObjectJson(gson, key, value)
    }

    override fun read(): T? {
        val dataJson = preferences.getString(key)
        return gson.fromJsonSafe(dataJson, type, logsConsumer)
    }
}