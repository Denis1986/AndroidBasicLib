package com.denis1986.android.base.io

import com.denis1986.android.base.service.LogsConsumer
import com.denis1986.android.base.util.extensions.GsonExtensions.fromJsonSafe
import com.google.gson.Gson

/** Always reads data from file without caching.
 * Created by denis.druzhinin on 20.08.2019.
 */
open class FileJsonProvider<T>(
        dataPath: String,
        private val dataType: Class<T>,
        private val gson: Gson,
        private val logsConsumer: LogsConsumer? = null
) : FileDataProvider<T>(dataPath) {

    override fun encode(data: T) = gson.toJson(data)

    /**
     * @return if data structure format was changed (one of fields has another type), null will be returned instead of throwing exception.
     */
    override fun decode(dataString: String) = gson.fromJsonSafe<T>(dataString, dataType, logsConsumer)
}