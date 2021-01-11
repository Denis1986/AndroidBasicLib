package com.denis1986.android.base.mvvm

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.denis1986.android.base.mvvm.data.LoadingInfo
import com.denis1986.android.base.mvvm.data.SnackbarEventInfo
import com.denis1986.android.base.util.ViewUtils
import com.google.android.material.snackbar.Snackbar

/** Implements common ViewModel's events handing.
 *
 * Created by denis.druzhinin on 10.10.2019.
 */
open class BasicViewModelEventHandlerImpl: BasicViewModelEventHandler {
    private var lastProgressDialog: ProgressDialog? = null
    private var lastSnackBar: Snackbar? = null

    override fun showToast(context: Context, text: String): Boolean {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        return true
    }

    override fun handleSnackbarEvent(snackbarEventInfo: SnackbarEventInfo): Boolean {
        val coordinatorLayout = getCoordinatorLayout()
                ?: return false

        if (!snackbarEventInfo.show && lastSnackBar != null) {
            lastSnackBar!!.dismiss()
            lastSnackBar = null
        } else if (snackbarEventInfo.show) {
            val snackbar = if (snackbarEventInfo.textFormatArgs == null) {
                Snackbar.make(coordinatorLayout, snackbarEventInfo.textResId, snackbarEventInfo.duration)
            } else {
                val text = coordinatorLayout.context.getString(snackbarEventInfo.textResId, *snackbarEventInfo.textFormatArgs.toTypedArray())
                Snackbar.make(coordinatorLayout, text, snackbarEventInfo.duration)
            }
            if (snackbarEventInfo.actionResId != null) {
                snackbar.setAction(snackbarEventInfo.actionResId, ViewUtils.emptyClickListener)
            }
            // Set anchor view if needed.
            getSnackbarAnchorView()?.let {
                snackbar.setAnchorView(coordinatorLayout)
            }
            // Show
            snackbar.show()
            if (snackbarEventInfo.duration == Snackbar.LENGTH_INDEFINITE) {
                lastSnackBar = snackbar
            }
        }
        return true
    }

    override fun showLoading(context: Context, loadingInfo: LoadingInfo): Boolean {
        if (loadingInfo.isShown && loadingInfo.titleResId != null) {
            lastProgressDialog = ProgressDialog.show(context, context.getString(loadingInfo.titleResId), null, true, loadingInfo.cancelable)
            if (loadingInfo.cancelListener != null) {
                lastProgressDialog?.setOnCancelListener(loadingInfo.cancelListener)
            }
        } else {
            lastProgressDialog?.dismiss()
        }
        return true
    }

    override fun getSnackbarAnchorView(): View? = null

    override fun getCoordinatorLayout(): CoordinatorLayout? = null
}