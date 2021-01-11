package com.denis1986.android.base.io

/** It is an degenerated case of [FileDataProvider] for saving and reading a raw string.
 *
 * Created by Denis Druzhinin on 25.11.2020.
 */
open class FileStringProvider(dataPath: String
) : FileDataProvider<String>(dataPath) {

    override fun encode(data: String) = data

    override fun decode(dataString: String) = dataString
}