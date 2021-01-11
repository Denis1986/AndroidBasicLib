package com.denis1986.android.base.util.extensions

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/** A set of extension methods for providing ViewModel via [androidx.lifecycle.ViewModelProvider] for a given Fragment or Activity.
 *
 * Created by denis.druzhinin on 09.10.2019.
 */

/**
 * @return ViewModel instance for a given fragment. This viewModel will be able to save its state on process recreation.
 */
inline fun <reified VM: ViewModel> Fragment.viewModelProviderWithSavedState(app: Application,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE) = lazy(mode) {
    val saveStateFactory = SavedStateViewModelFactory(app, this)
    val provider = ViewModelProvider(this, saveStateFactory)
    provider.get(VM::class.java)
}

/**
 * @return ViewModel instance for a given activity. This viewModel will be able to save its state on process recreation.
 */
inline fun <reified VM: ViewModel> FragmentActivity.viewModelProviderWithSavedState(app: Application,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE) = lazy(mode) {
    val saveStateFactory = SavedStateViewModelFactory(app, this)
    val provider = ViewModelProvider(this, saveStateFactory)
    provider.get(VM::class.java)
}

/**
 * @return ViewModel instance for an activity, associated with a given fragment. This viewModel will be able to save its state on process recreation.
 */
inline fun <reified VM: ViewModel> Fragment.activityViewModelProviderWithSavedState(app: Application,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE) = lazy(mode) {
    val owner = this.context as FragmentActivity
    val saveStateFactory = SavedStateViewModelFactory(app, this)
    val provider = ViewModelProvider(owner, saveStateFactory)
    provider.get(VM::class.java)
}

/**
 * @return ViewModel instance for a given fragment.
 */
inline fun <reified VM: ViewModel> Fragment.viewModelProvider(
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
        factory: ViewModelProvider.Factory? = null) = lazy(mode) {
    val provider = if (factory == null) ViewModelProvider(this) else ViewModelProvider(this, factory)
    provider.get(VM::class.java)
}

/**
 * @return ViewModel instance for a given activity.
 */
inline fun <reified VM: ViewModel> FragmentActivity.viewModelProvider(
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
        factory: ViewModelProvider.Factory? = null) = lazy(mode) {
    val provider = if (factory == null) ViewModelProvider(this) else ViewModelProvider(this, factory)
    provider.get(VM::class.java)
}

/**
 * @return ViewModel instance for an activity, associated with a given fragment.
 */
inline fun <reified VM: ViewModel> Fragment.activityViewModelProvider(
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
        factory: ViewModelProvider.Factory? = null) = lazy(mode) {
    val owner = this.context as FragmentActivity
    val provider = if (factory == null) ViewModelProvider(owner) else ViewModelProvider(owner, factory)
    provider.get(VM::class.java)
}