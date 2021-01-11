package com.denis1986.android.base.io

import com.google.gson.Gson
import com.denis1986.android.base.service.LogsConsumer

/** Gives an ability to save complex data structures to file.
 *
 * Created by Denis Druzhinin on 04.07.2020.
 */
class FileSaver<T>(filePath: String, dataType: Class<T>, gson: Gson = Gson(), logsConsumer: LogsConsumer? = null
): Saver<T> {

    private val fileDataProvider = FileJsonProvider(filePath, dataType, gson, logsConsumer)

    override fun save(value: T?) {
        fileDataProvider.putData(value)
    }

    override fun read(): T? {
        return fileDataProvider.getData()
    }
}