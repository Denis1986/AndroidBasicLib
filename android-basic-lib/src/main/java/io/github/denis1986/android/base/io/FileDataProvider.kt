package io.github.denis1986.android.base.io

import java.io.File

/** Always reads data from file without caching.
 * Created by denis.druzhinin on 20.08.2019.
 */
abstract class FileDataProvider<T>(
        protected val dataPath: String
) {

    fun getData(): T? {
        val inFile = File(dataPath)
        return if (inFile.exists()) {
            val dataString = inFile.bufferedReader().use {
                it.readText()
            }
            decode(dataString)
        } else null
    }

    fun putData(data: T?) {
        val outFile = File(dataPath)
        if (data == null) {
            outFile.delete()
        } else {
            val dataString = encode(data)
            if (!outFile.exists()) {
                outFile.parentFile?.mkdirs()
                outFile.createNewFile()
            }
            outFile.writeText(dataString)
        }
    }

    protected abstract fun encode(data: T): String
    protected abstract fun decode(dataString: String): T?
}