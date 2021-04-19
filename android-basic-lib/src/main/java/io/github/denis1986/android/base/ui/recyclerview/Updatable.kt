package io.github.denis1986.android.base.ui.recyclerview

/**
 * Created by Denis Druzhinin on 25.05.2020.
 */
interface Updatable<T> {
    fun update(item: T)
    fun update(items: List<T>, position: Int) { }
}