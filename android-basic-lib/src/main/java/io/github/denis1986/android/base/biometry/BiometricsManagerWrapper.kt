package io.github.denis1986.android.base.biometry

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Created by denis.druzhinin on 25.12.2019.
 */
class BiometricsManagerWrapper(private val promptParams: BiometricPromptParams) {
    private var biometricPrompt: BiometricPrompt? = null

    var isStarted: Boolean = false

    fun startAuthentication(activity: FragmentActivity, callback: BiometricPrompt.AuthenticationCallback): Int {
        val biometricManager = BiometricManager.from(activity)
        val canAuthenticateResult = biometricManager.canAuthenticate()

        if (canAuthenticateResult == BiometricManager.BIOMETRIC_SUCCESS) {
            biometricPrompt = BiometricPrompt(activity, ContextCompat.getMainExecutor(activity), AuthenticationCallbackDecoratorImpl(callback))
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(activity.getString(promptParams.titleResId))
                    .setSubtitle(activity.getString(promptParams.descriptionResId))
                    .setNegativeButtonText(activity.getString(promptParams.negativeButtonTextResId))
                    .setConfirmationRequired(false)
                    .build()
            biometricPrompt!!.authenticate(promptInfo)
            isStarted = true
        }
        return canAuthenticateResult
    }

    fun cancel() {
        biometricPrompt?.cancelAuthentication()
        isStarted = false
    }

    companion object {
        fun isBiometricsSupported(context: Context): Boolean {
            val biometricManager = BiometricManager.from(context)
            return biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
        }
    }

    private inner class AuthenticationCallbackDecoratorImpl(callback: BiometricPrompt.AuthenticationCallback) : AuthenticationCallbackDecorator(callback) {

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)

            isStarted = false
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)

            isStarted = false
        }
    }
}