package io.github.denis1986.android.base.io

/**
 * Created by Denis Druzhinin on 04.07.2020.
 */
class BasicSaver<T>(private val readFunction: () -> T?,
                    private val writeFunction: (T?) -> Unit
) : Saver<T> {

    override fun save(value: T?) {
        writeFunction.invoke(value)
    }

    override fun read(): T? {
        return readFunction.invoke()
    }
}