package io.github.denis1986.android.base.sample.screens.main

import android.content.Intent
import android.os.Bundle
import io.github.denis1986.android.base.mvvm.component.BasicActivityWithoutViewModel
import io.github.denis1986.android.base.sample.R
import io.github.denis1986.android.base.sample.screens.biometry.BiometryActivity
import io.github.denis1986.android.base.sample.screens.list.UserListActivity
import io.github.denis1986.android.base.sample.screens.persistent.PersistentPropertiesActivity
import io.github.denis1986.android.base.sample.screens.retry.RetryActivity

import kotlinx.android.synthetic.main.content_main.*

/** Launcher activity, which gives an ability to choose a concrete sample.
 *
 * Created by Denis Druzhinin on 07.01.2021.
 */
class MainActivity : BasicActivityWithoutViewModel() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        super.initializeViews(savedInstanceState)

        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun addViewListeners() {
        super.addViewListeners()

        persistentPropertiesButton.setOnClickListener {
            startActivity(Intent(this, PersistentPropertiesActivity::class.java))
        }

        biometryButton.setOnClickListener {
            startActivity(Intent(this, BiometryActivity::class.java))
        }

        recyclerViewAdapterButton.setOnClickListener {
            startActivity(Intent(this, UserListActivity::class.java))
        }

        retryScreenButton.setOnClickListener {
            startActivity(Intent(this, RetryActivity::class.java))
        }
    }
}