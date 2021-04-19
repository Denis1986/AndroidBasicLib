package io.github.denis1986.android.base.biometry

import android.app.Application
import android.util.Log
import androidx.biometric.BiometricPrompt
import io.github.denis1986.android.base.mvvm.MutableLiveDataWrapper
import io.github.denis1986.android.base.mvvm.component.BasicViewModel
import io.github.denis1986.android.base.mvvm.data.Event
import io.github.denis1986.android.base.service.LogsConsumer

/**
 * Created by denis.druzhinin on 04.10.2019.
 */
abstract class BiometricsContainerScreenModel(
    app: Application,
    private val logsConsumer: LogsConsumer? = null
) : BasicViewModel(app) {

    protected var biometricsManager: BiometricsManagerWrapper? = null
        private set

    val startBiometricsLoginEvent = MutableLiveDataWrapper<Event<BiometricsManagerWrapper>>()

    override fun onCleared() {
        super.onCleared()
        stopBiometricManager()
    }

    override fun onViewStop() {
        stopBiometricManager()
    }

    protected fun stopBiometricManager() {
        biometricsManager?.cancel()
    }

    protected abstract fun onBiometricAuthenticationSucceeded()

    /**
     * Starts BiometricManager if it is not currently started.
     */
    protected open fun startBiometricManagerIfNecessary(promptParams: BiometricPromptParams) {
        if (biometricsManager?.isStarted == true)
            return

        startTouchIdAuthentication(promptParams)
    }

    fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        if (errorCode == BiometricPrompt.ERROR_CANCELED)
            return

        if (errorCode == BiometricPrompt.ERROR_USER_CANCELED || errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
            onCancelledBiometricsConfirmation() }
        else {
            logWarning("Error code: $errorCode. Error message: $errString")
            biometricsManager?.cancel()
        }
    }

    protected open fun onCancelledBiometricsConfirmation() { }

    fun onAuthenticationSucceeded() {
        onBiometricAuthenticationSucceeded()
    }

    fun onAuthenticationFailed() {
        logWarning( "Failed to recognize touch ID.")
    }

    private fun startTouchIdAuthentication(promptParams: BiometricPromptParams) {
        biometricsManager = BiometricsManagerWrapper(promptParams)
        startBiometricsLoginEvent.asMutable().value = Event(biometricsManager!!)
    }

    private fun logWarning(message: String) {
        if (logsConsumer == null) {
            Log.w(TAG, message)
        } else {
            logsConsumer.e(TAG, message)
        }
    }

    companion object {
        private val TAG = BiometricsContainerScreenModel::class.simpleName!!
    }
}