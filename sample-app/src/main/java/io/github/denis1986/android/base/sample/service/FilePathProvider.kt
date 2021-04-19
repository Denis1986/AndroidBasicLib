package io.github.denis1986.android.base.sample.service

import android.content.Context
import java.io.File

/**
 * Created by Denis Druzhinin on 03.01.2021.
 */
class FilePathProvider(private val context: Context) {

    fun getCurrentUserFilePath() = "${getDataPath()}${File.separator}currentUser.json"

    fun getDataPath(): String {
        return "${context.getExternalFilesDir(null)!!.path}${File.separator}data"
    }
}