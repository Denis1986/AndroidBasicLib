package io.github.denis1986.android.base.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/** A wrapper for MutableLiveData. Normally, ViewModel changes LiveData value, Fragment or Activity listens for these changes. This class gives an ability to distinguish these
 * use cases, because it is necessary to call [asMutable] method directly to change LiveData's value. This approach helps to avoid cases, when LiveData's value is changed
 * in place where it should be only read.
 *
 * Created by Denis Druzhinin on 10.08.2020.
 */
class MutableLiveDataWrapper<T>(initialValue: T? = null) {

    // If initial null value is set for MutableLiveData, it would be propagated to listeners.
    private val value = if (initialValue == null) MutableLiveData<T>() else MutableLiveData<T>(initialValue)

    fun get(): LiveData<T> = value

    fun asMutable() = value

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        value.observe(owner, observer)
    }
}