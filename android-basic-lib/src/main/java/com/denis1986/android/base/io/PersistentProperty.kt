package com.denis1986.android.base.io

import com.denis1986.android.base.data.SynchronizedValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.concurrent.write

typealias Initializer<T> = () -> T?

/** Encapsulates logic of reading and saving value to persistent storage. Access is synchronized.
 *
 * Created by Denis Druzhinin on 04.07.2020.
 */
open class PersistentProperty<T>(private val saver: Saver<T>,
                            private val executor: CoroutineDispatcher,
                            private val persistValueFromInitializer: Boolean = false,
                            private val initializer: Initializer<T>? = null
) {
    private val synchronizedProperty = SynchronizedValue<T>()


    fun set(newValue: T?) {
        synchronizedProperty.write {
            saver.save(newValue)
            newValue
        }
    }

    suspend fun setAsync(newValue: T?) {
        withContext(executor) {
            synchronizedProperty.write {
                saver.save(newValue)
                newValue
            }
        }
    }

    fun update(action: (T?) -> Unit) {
        synchronizedProperty.update {
            initializeIfNecessary()
            action.invoke(synchronizedProperty.value)
            saver.save(synchronizedProperty.value)
        }
    }

    /**
     * Persists changes only if the specified *action* returns true.
     */
    fun updateIfNecessary(action: (T?) -> Boolean): Boolean {
        var isUpdated = false
        synchronizedProperty.update {
            initializeIfNecessary()
            isUpdated = action.invoke(synchronizedProperty.value)
            if (isUpdated) {
                saver.save(synchronizedProperty.value)
            }
        }
        return isUpdated
    }

    suspend fun updateAsync(action: (T?) -> Unit) {
        withContext(executor) {
            synchronizedProperty.update {
                initializeIfNecessary()
                action.invoke(synchronizedProperty.value)
                saver.save(synchronizedProperty.value)
            }
        }
    }

    fun get(): T? {
        initializeWithSynchronization()
        return synchronizedProperty.get()
    }

    suspend fun getAsync(): T? {
        withContext(executor) {
            initializeWithSynchronization()
        }
        return synchronizedProperty.get()
    }

    suspend fun getAsync(asyncInitializer: suspend () -> T): T? {
        withContext(executor) {
            if (synchronizedProperty.get() == null) {
                synchronizedProperty.dataLock.write {
                    // Double check for null because potentially synchronizedProperty value could change after the first check.
                    if (synchronizedProperty.value == null) {
                        synchronizedProperty.value = saver.read() ?: asyncInitializer.invoke()
                    }
                }
            }
        }
        return synchronizedProperty.get()
    }

    fun read(action: (T?) -> Unit) {
        initializeWithSynchronization()
        synchronizedProperty.read(action)
    }

    suspend fun readAsync(action: (T?) -> Unit) {
        withContext(executor) {
            initializeWithSynchronization()
        }
        synchronizedProperty.read(action)
    }

    private fun initializeWithSynchronization() {
        if (synchronizedProperty.get() == null) {
            synchronizedProperty.dataLock.write {
                initializeIfNecessary()
            }
        }
    }

    // Note: Access to synchronizedProperty must be synchronized from outside.
    private fun initializeIfNecessary() {
        if (synchronizedProperty.value == null) {
            synchronizedProperty.value = saver.read()
            if (synchronizedProperty.value == null) {
                synchronizedProperty.value = initializer?.invoke()
                if (synchronizedProperty.value != null && persistValueFromInitializer) {
                    saver.save(synchronizedProperty.value)
                }
            }
        }
    }
}