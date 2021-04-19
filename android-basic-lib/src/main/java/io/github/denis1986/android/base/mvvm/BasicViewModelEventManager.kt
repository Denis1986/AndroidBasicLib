package io.github.denis1986.android.base.mvvm

import androidx.lifecycle.LifecycleOwner
import io.github.denis1986.android.base.mvvm.component.BasicActivity
import io.github.denis1986.android.base.mvvm.component.BasicViewModel
import java.lang.ref.WeakReference

/**
 * Created by Denis Druzhinin on 17.04.2020.
 */
class BasicViewModelEventManager<VM> where VM: BasicViewModel {

    private var viewModelEventHandler: BasicViewModelEventHandler? = null
    private var activityRef: WeakReference<BasicActivity<*>>? = null

    fun addViewModelObservers(activity: BasicActivity<*>, lifecycleOwner: LifecycleOwner, viewModel: VM?, viewModelEventHandler: BasicViewModelEventHandler = BasicViewModelEventHandlerImpl()) {
        if (activity.viewModelObserversAdded && activity.viewModel === viewModel)
            return // Don't add observers twice.

        activityRef = WeakReference(activity)
        this.viewModelEventHandler = viewModelEventHandler

        viewModel?.run {
            showToastEvent.observe(lifecycleOwner, EventObserver { text ->
                activityRef?.get()?.let {
                    viewModelEventHandler.showToast(it, text)
                }
            })

            snackbarEvent.observe(lifecycleOwner, EventObserver { snackbarEventInfo ->
                viewModelEventHandler.handleSnackbarEvent(snackbarEventInfo)
            })

            showLoadingViewEvent.observe(lifecycleOwner, EventObserver { loadingInfo ->
                activityRef?.get()?.let {
                    viewModelEventHandler.showLoading(it, loadingInfo)
                }
            })
        }
    }
}