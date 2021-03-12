package com.denis1986.android.base.sample.screens.retry

import android.os.Bundle
import android.widget.SeekBar
import com.denis1986.android.base.mvvm.component.BasicActivity
import com.denis1986.android.base.sample.R
import com.denis1986.android.base.sample.SampleApplication
import com.denis1986.android.base.sample.base.OnSeekBarChangeListenerAdapter
import com.denis1986.android.base.util.extensions.viewModelProviderWithSavedState
import kotlinx.android.synthetic.main.activity_biometry.*
import kotlinx.android.synthetic.main.content_retry.*

/** Demonstrates how to create and use a subclass of [com.denis1986.android.base.network.retry.RetryableExecutor] and how to take into account connection state with the help of
 * [com.denis1986.android.base.network.NetworkStateManager].
 *
 * Created by Denis Druzhinin on 08.01.2021.
 */
class RetryActivity : BasicActivity<RetryScreenModel>() {

    override val viewModel by viewModelProviderWithSavedState<RetryScreenModel>(SampleApplication.instance)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retry)
    }

    override fun addViewModelObservers() {
        super.addViewModelObservers()

        viewModel.log.observe(this) { log ->
            logView.text = log
        }
    }

    override fun addViewListeners() {
        super.addViewListeners()

        sendRequestButton.setOnClickListener {
            val retryParam = RetryParam(
                failedAttemptCount = failedAttemptCountView.progress,
                takeIntoAccountNetworkState = takeIntoAccountNetworkStateView.isChecked
            )
            viewModel.onSendRequestButtonClicked(retryParam)
        }

        cancelButton.setOnClickListener {
            viewModel.onCancelButtonClicked()
        }

        failedAttemptCountView.setOnSeekBarChangeListener(object: OnSeekBarChangeListenerAdapter() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateTakeIntoAccountNetworkStateViewLabel(progress)
            }
        })
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        super.initializeViews(savedInstanceState)

        setSupportActionBar(toolbar)
        displayHomeAsUp(true)

        updateTakeIntoAccountNetworkStateViewLabel(failedAttemptCountView.progress)
    }

    private fun updateTakeIntoAccountNetworkStateViewLabel(progress: Int) {
        val newLabel = getString(R.string.failed_attempt_count, progress)
        failedAttemptCountLabelView.text = newLabel
    }
}