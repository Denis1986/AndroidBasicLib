package io.github.denis1986.android.base.sample.screens.persistent

import android.os.Bundle
import io.github.denis1986.android.base.mvvm.component.BasicActivity
import io.github.denis1986.android.base.sample.R
import io.github.denis1986.android.base.util.extensions.viewModelProvider
import kotlinx.android.synthetic.main.activity_persistent_properties.*
import kotlinx.android.synthetic.main.content_persistent_properties.*

/** Demonstrates how to save data persistently to preferences and to file with the help of [io.github.denis1986.android.base.io.PersistentProperty].
 *
 * Created by Denis Druzhinin on 07.01.2021.
 */
class PersistentPropertiesActivity : BasicActivity<PersistentPropertiesScreenViewModel>() {

    override val viewModel by viewModelProvider<PersistentPropertiesScreenViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persistent_properties)
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        super.initializeViews(savedInstanceState)

        setSupportActionBar(toolbar)
        displayHomeAsUp(true)
    }

    override fun addViewModelObservers() {
        super.addViewModelObservers()

        viewModel.userFromPreferences.observe(this) { user ->
            preferencesPropertyEdit.setText(user.name)
        }

        viewModel.userFromFile.observe(this) { user ->
            filePropertyEdit.setText(user.name)
        }
    }

    override fun addViewListeners() {
        super.addViewListeners()

        reloadFilePropertyButton.setOnClickListener {
            viewModel.onReloadFilePropertyButtonClicked()
        }

        updateFilePropertyButton.setOnClickListener {
            viewModel.onUpdateFilePropertyButtonClicked(filePropertyEdit.text?.toString())
        }

        reloadPreferencesPropertyButton.setOnClickListener {
            viewModel.onReloadPreferencesPropertyButtonClicked()
        }

        updatePreferencesPropertyButton.setOnClickListener {
            viewModel.onUpdatePreferencesPropertyButton(preferencesPropertyEdit.text?.toString())
        }
    }
}