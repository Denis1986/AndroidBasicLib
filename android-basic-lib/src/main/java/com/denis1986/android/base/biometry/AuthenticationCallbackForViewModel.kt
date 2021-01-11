package com.denis1986.android.base.biometry

import androidx.biometric.BiometricPrompt

/** Notifies a given ViewModel about AuthenticationCallback's events.
 *
 * Created by Denis Druzhinin on 20.02.2020.
 */
class AuthenticationCallbackForViewModel(private val viewModel: BiometricsContainerScreenModel
) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        viewModel.onAuthenticationError(errorCode, errString)
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        viewModel.onAuthenticationSucceeded()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        viewModel.onAuthenticationFailed()
    }
}