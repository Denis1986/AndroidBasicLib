package com.denis1986.android.base.data

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/** Access to a given value is synchronized via [java.util.concurrent.locks.ReentrantReadWriteLock].
 *
 * Created by denis.druzhinin on 24.10.2019.
 */
class SynchronizedValue<T>(value: T? = null) {
    var value: T? = null
    val dataLock = ReentrantReadWriteLock(true)

    init {
        if (value != null) {
            set(value)
        }
    }

    fun set(value: T?) {
        dataLock.write {
            this.value = value
        }
    }

    fun write(action: () -> T?): SynchronizedValue<T> {
        dataLock.write {
            this.value = action()
        }
        return this
    }

    fun update(action: (T?) -> Unit) {
        dataLock.write {
            action(value)
        }
    }

    fun get(): T? {
        return dataLock.read {
            value
        }
    }

    fun read(action: (T?) -> Unit) {
        dataLock.read {
            action.invoke(value)
        }
    }

    fun compareAndSet(expect: T?, update: T?): Boolean {
        dataLock.write {
            if (value == expect) {
                value = update
                return true
            }
        }
        return false
    }
}