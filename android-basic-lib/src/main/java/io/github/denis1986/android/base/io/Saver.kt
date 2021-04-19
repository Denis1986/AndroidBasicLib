package io.github.denis1986.android.base.io

/**
 * Created by Denis Druzhinin on 04.07.2020.
 */
interface Saver<T> {
    fun save(value: T?)
    fun read(): T?
}