package io.github.denis1986.android.base.biometry

import androidx.annotation.StringRes

/**
 * Created by denis.druzhinin on 25.12.2019.
 */
data class BiometricPromptParams(
        @StringRes val titleResId: Int,
        @StringRes val descriptionResId: Int,
        @StringRes val negativeButtonTextResId: Int
)