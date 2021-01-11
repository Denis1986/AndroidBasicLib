package com.denis1986.android.base.mvvm.data

import android.content.DialogInterface
import androidx.annotation.StringRes

/**
 * Created by denis.druzhinin on 10.10.2019.
 */
typealias DialogCancelListener = (DialogInterface?) -> Unit

data class LoadingInfo(
        val isShown: Boolean,
        @StringRes val titleResId: Int? = null,
        val cancelable: Boolean = false,
        val cancelListener: DialogCancelListener? = null
)