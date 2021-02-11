package com.denis1986.android.base.io

import androidx.lifecycle.SavedStateHandle

/** Encapsulates SavedStateHandle interaction for saving and restoring single enum value.
 *
 * Created by Denis Druzhinin on 10.02.2021.
 */
class ViewModelPersistentStateEnumItem<T: Enum<*>> (
        private val savedStateHandle: SavedStateHandle,
        private val key: String,
        private val allValuesProvider: () -> Array<T>,
        private val initializer: Initializer<T>? = null
) {
    fun set(newValue: T?) = savedStateHandle.set(key, newValue?.ordinal)

    fun get(): T? = (savedStateHandle.get(key) as Int?)?.let { allValuesProvider.invoke()[it] } ?: initializer?.invoke()

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