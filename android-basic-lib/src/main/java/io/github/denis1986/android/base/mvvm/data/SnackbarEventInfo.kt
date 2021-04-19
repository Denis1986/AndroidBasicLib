package io.github.denis1986.android.base.mvvm.data

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * Created by denis.druzhinin on 13.09.2019.
 */
data class SnackbarEventInfo(
        @StringRes val textResId: Int,
        @SnackbarDuration val duration: Int = Snackbar.LENGTH_LONG,
        @StringRes val actionResId: Int? = null,
        val show: Boolean = true,
        val textFormatArgs: List<Any>? = null
) {

    companion object {

        fun showShort(
                @StringRes textResId: Int,
                @StringRes actionResId: Int? = null
        ): SnackbarEventInfo {
            return SnackbarEventInfo(
                    textResId = textResId,
                    duration = Snackbar.LENGTH_SHORT,
                    actionResId = actionResId,
                    show = true
            )
        }

        fun showLong(
                @StringRes textResId: Int,
                @StringRes actionResId: Int? = null
        ): SnackbarEventInfo {
            return SnackbarEventInfo(
                    textResId = textResId,
                    duration = Snackbar.LENGTH_LONG,
                    actionResId = actionResId,
                    show = true
            )
        }

        fun showIndefinite(
                @StringRes textResId: Int,
                @StringRes actionResId: Int? = null
        ): SnackbarEventInfo {
            return SnackbarEventInfo(
                    textResId = textResId,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    actionResId = actionResId,
                    show = true
            )
        }

        fun hide(): SnackbarEventInfo {
            return SnackbarEventInfo(
                    textResId = 0,
                    show = false
            )
        }
    }
}