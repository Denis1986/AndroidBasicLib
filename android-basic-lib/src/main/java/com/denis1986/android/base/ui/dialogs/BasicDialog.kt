package com.denis1986.android.base.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.denis1986.android.base.R

/**
 * Created by denis.druzhinin on 21.01.2020.
 */
open class BasicDialog(context: Context, private val isModal: Boolean, @StyleRes styleResId: Int): Dialog(context, styleResId) {
    private val simplyCloseClickListener = View.OnClickListener { dismiss() }

    protected open fun initializeViews(@IdRes topLevelContainerId: Int = R.id.topLevelContainer) {
        if (!isModal) {
            val topLevelContainer = findViewById<View>(topLevelContainerId)
            topLevelContainer.setOnClickListener(simplyCloseClickListener)
        }
    }
}