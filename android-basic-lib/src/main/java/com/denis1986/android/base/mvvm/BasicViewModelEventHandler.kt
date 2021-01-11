package com.denis1986.android.base.mvvm

import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.denis1986.android.base.mvvm.data.LoadingInfo
import com.denis1986.android.base.mvvm.data.SnackbarEventInfo

/**
 * Created by Denis Druzhinin on 17.04.2020.
 */
interface BasicViewModelEventHandler {
    fun showToast(context: Context, text: String): Boolean

    fun handleSnackbarEvent(snackbarEventInfo: SnackbarEventInfo): Boolean

    fun showLoading(context: Context, loadingInfo: LoadingInfo): Boolean

    fun getSnackbarAnchorView(): View?

    fun getCoordinatorLayout(): CoordinatorLayout?
}