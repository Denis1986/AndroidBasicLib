package io.github.denis1986.android.base.util

import android.content.Context
import android.view.View
import kotlin.math.roundToInt

/**
 * Created by denis.druzhinin on 14.10.2019.
 */
object ViewUtils {

    @JvmStatic
    val emptyClickListener = View.OnClickListener { }

    fun dipsToPixels(context: Context, dips: Float): Int {
        return (dips * context.resources.displayMetrics.density).roundToInt()
    }
}