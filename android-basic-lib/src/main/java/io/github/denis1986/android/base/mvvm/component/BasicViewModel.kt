package io.github.denis1986.android.base.mvvm.component

import android.app.Application
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.denis1986.android.base.mvvm.data.*
import io.github.denis1986.android.base.network.NetworkUtils
import io.github.denis1986.android.base.util.ThreadUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Created by denis.druzhinin on 13.08.2019.
 */
open class BasicViewModel(app: Application) : AndroidViewModel(app) {

    private val _finishActivityEvent = MutableLiveData<Event<ActivityResult>>()
    val finishActivityEvent: LiveData<Event<ActivityResult>> = _finishActivityEvent

    private val _showLoadingViewEvent = MutableLiveData<Event<LoadingInfo>>()
    val showLoadingViewEvent: LiveData<Event<LoadingInfo>> = _showLoadingViewEvent

    private val _showToastsEvent = MutableLiveData<Event<String>>()
    val showToastEvent: LiveData<Event<String>> = _showToastsEvent

    private val _snackbarEvent = MutableLiveData<Event<SnackbarEventInfo>>()
    val snackbarEvent: LiveData<Event<SnackbarEventInfo>> = _snackbarEvent

    private var lastDebouncedActionTimestamp: Long = 0

    @Volatile
    protected var isFinishActivityEventPosted = false
        private set

    private val loadingLogic = LoadingLogic()

    protected var isPerformingBackgroundAction = false

    @StringRes
    protected var messageNoInternetResId: Int? = null

    private val delayedJobMap = HashMap<String, Job>()

    protected fun cancelDelayed(actionTag: String = DEFAULT_DELAYED_ACTION_TAG) {
        delayedJobMap.remove(actionTag)
    }

    /**
     * Previous action with the same tag is canceled.
     */
    protected fun runDelayed(delayMs: Long = DEFAULT_ACTION_DELAY_MS, actionTag: String = DEFAULT_DELAYED_ACTION_TAG, action: suspend () -> Unit) {
        delayedJobMap[actionTag]?.cancel()
        delayedJobMap[actionTag] = viewModelScope.launch {
            delay(delayMs)
            if (isActive) {
                action()
            }
        }
    }

    /**
     * Prevents too often execution of a given action.
     */
    protected fun runDebounced(debounceTimeoutMs: Long = DEBOUNCE_TIMEOUT_MS, action: () -> Unit) {
        if (!checkAndUpdateDebouncedActionTimestamp(debounceTimeoutMs))
            return

        action()
    }

    /**
     * Prevents too often execution of a given action.
     */
    protected fun runDebouncedAsCoroutine(debounceTimeoutMs: Long = DEBOUNCE_TIMEOUT_MS, action: suspend () -> Unit) {
        if (!checkAndUpdateDebouncedActionTimestamp(debounceTimeoutMs))
            return

        viewModelScope.launch {
            action()
        }
    }

    private fun checkAndUpdateDebouncedActionTimestamp(debounceTimeoutMs: Long): Boolean {
        val now = System.currentTimeMillis()
        if (isVerbose) Log.i(TAG, "Actions timestamps difference: ${now - lastDebouncedActionTimestamp}")
        if (now - lastDebouncedActionTimestamp < debounceTimeoutMs) {
            if (isVerbose) Log.i(TAG, "Cancel too frequent action")
            return false
        }

        lastDebouncedActionTimestamp = now
        return true
    }

    @StringRes
    protected open fun getSpecificErrorMessageResId(throwable: Throwable?): Int? {
        return when {
            NetworkUtils.isNoInternetException(throwable) && messageNoInternetResId != null -> messageNoInternetResId
            throwable is CodeContainer -> getMessageResIdByErrorCode(throwable.code)
            else -> null
        }
    }

    @StringRes
    protected open fun getMessageResIdByErrorCode(errorCode: Int): Int? = null

    protected fun showErrorMessage(throwable: Throwable?, @StringRes defaultMessageResId: Int) {
        val messageResId = getSpecificErrorMessageResId(throwable)
                ?: defaultMessageResId
        postSnackbarEvent(SnackbarEventInfo.showLong(messageResId))
    }

    protected fun postFinishActivityEvent(resultCode: Int? = null) {
        postEvent(_finishActivityEvent, ActivityResult(resultCode))
        isFinishActivityEventPosted = true
    }

    fun postSnackbarEvent(info: SnackbarEventInfo) {
        postEvent(_snackbarEvent, info)
    }

    /**
     * LoadingView will not be hidden earlier than after a minimal time period (500 ms by default) in order to avoid flickering.
     */
    protected fun postHideLoadingViewEvent(minimalTimePeriodToShow: Long = SHOW_LOADING_MIN_PERIOD_MS) {
        loadingLogic.postHideLoadingViewEvent(LoadingInfo(false), minimalTimePeriodToShow)
    }

    protected fun postShowLoadingViewEvent(loadingInfo: LoadingInfo) {
        loadingLogic.postShowLoadingViewEvent(loadingInfo)
    }

    private inner class LoadingLogic {
        private var startToShowLoadingMs: Long = 0
        private var delayedHideLoadingJob: Job? = null

        fun postHideLoadingViewEvent(loadingInfo: LoadingInfo, minimalTimePeriodToShow: Long) {
            if (delayedHideLoadingJob != null)
                return

            // We are going to hide Loading view.
            val loadingShownPeriodMs = System.currentTimeMillis() - startToShowLoadingMs
            // Set minimal time period, when Loading view is shown in order to avoid flickering.
            if (loadingShownPeriodMs < minimalTimePeriodToShow) {
                delayedHideLoadingJob = viewModelScope.launch {
                    delay(minimalTimePeriodToShow - loadingShownPeriodMs)
                    if (isActive) {
                        postEvent(_showLoadingViewEvent, loadingInfo)
                        delayedHideLoadingJob = null
                    }
                }
            } else {
                postEvent(_showLoadingViewEvent, loadingInfo)
            }
        }

        fun postShowLoadingViewEvent(loadingInfo: LoadingInfo) {
            startToShowLoadingMs = System.currentTimeMillis()
            delayedHideLoadingJob?.cancel()
            delayedHideLoadingJob = null
            postEvent(_showLoadingViewEvent, loadingInfo)
        }
    }

    @Deprecated("Use Snackbar events instead")
    protected fun postShowToastEvent(text: String) {
        postEvent(_showToastsEvent, text)
    }

    open fun onViewStart() {
    }

    open fun onViewStop() {
    }

    open fun onToolbarBack(): Boolean = false

    open fun onBack(): Boolean = false

    private fun <T> postEvent(event: MutableLiveData<Event<T>>, value: T) {
        if (ThreadUtils.getInstance().isUiThread()) {
            event.value = Event(value)
        } else {
            event.postValue(Event(value))
        }
    }

    private fun postEvent(event: MutableLiveData<ContentlessEvent>) {
        if (ThreadUtils.getInstance().isUiThread()) {
            event.value = ContentlessEvent()
        } else {
            event.postValue(ContentlessEvent())
        }
    }


    private companion object {
        const val DEBOUNCE_TIMEOUT_MS: Long = 700
        const val DEFAULT_ACTION_DELAY_MS: Long = 150
        const val SHOW_LOADING_MIN_PERIOD_MS: Long = 500

        const val DEFAULT_DELAYED_ACTION_TAG = "DefaultDelayedActionTag"

        val TAG = BasicViewModel::class.simpleName!!
        const val isVerbose = false
    }
}