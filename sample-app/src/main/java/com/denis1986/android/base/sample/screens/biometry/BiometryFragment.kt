package com.denis1986.android.base.sample.screens.biometry

import com.denis1986.android.base.biometry.AuthenticationCallbackForViewModel
import com.denis1986.android.base.mvvm.EventObserver
import com.denis1986.android.base.mvvm.component.BasicFragment
import com.denis1986.android.base.sample.R
import com.denis1986.android.base.sample.SampleApplication
import com.denis1986.android.base.util.extensions.viewModelProviderWithSavedState
import kotlinx.android.synthetic.main.fragment_biometry.*

/**
 * Created by Denis Druzhinin on 05.01.2021.
 */
class BiometryFragment : BasicFragment<BiometryScreenModel>() {

    override val viewModel by viewModelProviderWithSavedState<BiometryScreenModel>(SampleApplication.instance)

    override fun getLayoutResId() = R.layout.fragment_biometry

    override fun addViewModelObservers() {
        super.addViewModelObservers()

        viewModel.startBiometricsLoginEvent.observe(this, EventObserver { biometricsManager ->
            biometricsManager.startAuthentication(activity!!, AuthenticationCallbackForViewModel(viewModel))
        })

        viewModel.isLoginPerformed.observe(this) { isLoginPerformed ->
            infoView.setText(if (isLoginPerformed) R.string.logged_in_successfully else R.string.login_not_performed)
            loginButton.isEnabled = !isLoginPerformed
        }
    }

    override fun addViewListeners() {
        super.addViewListeners()

        loginButton.setOnClickListener {
            viewModel.onLoginButtonClicked()
        }
    }
}