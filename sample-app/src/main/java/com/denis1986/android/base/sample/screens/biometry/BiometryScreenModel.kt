package com.denis1986.android.base.sample.screens.biometry

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.denis1986.android.base.io.ViewModelPersistentStateItem
import com.denis1986.android.base.biometry.BiometricPromptParams
import com.denis1986.android.base.biometry.BiometricsContainerScreenModel
import com.denis1986.android.base.mvvm.MutableLiveDataWrapper
import com.denis1986.android.base.sample.R

/**
 * Created by Denis Druzhinin on 05.01.2021.
 */
class BiometryScreenModel(app: Application,
                          savedStateHandle: SavedStateHandle
) : BiometricsContainerScreenModel(app) {

    private val state = PersistentState(savedStateHandle)

    private val biometricPromptParams = BiometricPromptParams(
        titleResId = R.string.biometric_login_title,
        descriptionResId = R.string.biometric_login_description,
        negativeButtonTextResId = R.string.biometric_login_negative_button)

    private var isListeningForTouchId: Boolean = false

    val isLoginPerformed = MutableLiveDataWrapper<Boolean>()

    init {
        isLoginPerformed.asMutable().value = state.isLoginPerformed.get()
    }

    override fun onBiometricAuthenticationSucceeded() {
        updateIsLoginPerformed(true)
    }

    private fun updateIsLoginPerformed(isPerformed: Boolean) {
        state.isLoginPerformed.set(isPerformed)
        isLoginPerformed.asMutable().value = isPerformed
    }

    fun onLoginButtonClicked() {
        startBiometricManagerIfNecessary(biometricPromptParams)
        isListeningForTouchId = true
    }

    override fun onViewStart() {
        if (isListeningForTouchId) {
            startBiometricManagerIfNecessary(biometricPromptParams)
        }
    }

    private class PersistentState(savedStateHandle: SavedStateHandle) {

        val isLoginPerformed = ViewModelPersistentStateItem(savedStateHandle, KEY_IS_LOGIN_PERFORMED) { false }

        companion object {
            private const val KEY_IS_LOGIN_PERFORMED = "BiometryScreenModel:isLoginPerformed"
        }
    }
}