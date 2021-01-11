package com.denis1986.android.base.mvvm

import androidx.lifecycle.*

/** A wrapper for MediatorLiveData. Normally, ViewModel changes LiveData value, Fragment or Activity listens for these changes. This class gives an ability to distinguish these
 * use cases, because it is necessary to call [asMediator] or [asMutable] methods directly to change LiveData's value. This approach helps to avoid cases, when LiveData's value is changed
 * in place where it should be only read.
 *
 * Created by Denis Druzhinin on 10.08.2020.
 */
class MediatorLiveDataWrapper<T> {

    private val value = MediatorLiveData<T>()

    fun get(): LiveData<T> = value

    fun asMutable(): MutableLiveData<T> = value

    fun asMediator() = value

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        value.observe(owner, observer)
    }
}