package com.denis1986.android.base.mvvm.data

import androidx.annotation.IntDef
import androidx.annotation.IntRange
import com.google.android.material.snackbar.BaseTransientBottomBar

/**
 * Created by Denis Druzhinin on 06.03.2020.
 */
@IntDef(BaseTransientBottomBar.LENGTH_INDEFINITE, BaseTransientBottomBar.LENGTH_SHORT, BaseTransientBottomBar.LENGTH_LONG)
@IntRange(from = 1)
@Retention(AnnotationRetention.SOURCE)
annotation class SnackbarDuration