package io.github.denis1986.android.base.biometry

import androidx.biometric.BiometricPrompt

/**
 * Created by Denis Druzhinin on 06.01.2021.
 */
open class AuthenticationCallbackDecorator(private val callback: BiometricPrompt.AuthenticationCallback
) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        callback.onAuthenticationError(errorCode, errString)
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        callback.onAuthenticationSucceeded(result)
    }

    override fun onAuthenticationFailed() {
        callback.onAuthenticationFailed()
    }
}