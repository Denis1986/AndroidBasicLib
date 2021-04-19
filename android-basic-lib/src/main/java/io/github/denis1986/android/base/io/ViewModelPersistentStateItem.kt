package io.github.denis1986.android.base.io

import androidx.lifecycle.SavedStateHandle

/** Encapsulates SavedStateHandle interaction for saving and restoring single property value.
 * Created by Denis Druzhinin on 13.07.2020.
 */
class ViewModelPersistentStateItem<T> (private val savedStateHandle: SavedStateHandle,
                                       private val key: String,
                                       private val initializer: Initializer<T>? = null
) {
    fun set(newValue: T?) = savedStateHandle.set(key, newValue)

    fun get(): T? = savedStateHandle.get(key) ?: initializer?.invoke()

    fun setIfNull(newValue: T) {
        if (get() == null) {
            set(newValue)
        }
    }

    fun setIfNull(newValueProvider: Lazy<T>) {
        if (get() == null) {
            set(newValueProvider.value)
        }
    }
}