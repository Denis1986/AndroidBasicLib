package io.github.denis1986.android.base.sample.screens.biometry

import android.os.Bundle
import io.github.denis1986.android.base.mvvm.component.BasicFragmentsActivityWithoutViewModel
import io.github.denis1986.android.base.sample.R
import kotlinx.android.synthetic.main.activity_biometry.*

/** Demonstrates biometric login with the help of [io.github.denis1986.android.base.biometry.BiometricsManagerWrapper]. At the same time BiometryActivity is an example of class derived from
 *  [io.github.denis1986.android.base.mvvm.component.BasicFragmentsActivity]
 *
 * Created by Denis Druzhinin on 05.01.2021.
 */
class BiometryActivity : BasicFragmentsActivityWithoutViewModel() {

    override fun getInitialContentFragment() = BiometryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometry)
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        super.initializeViews(savedInstanceState)

        setSupportActionBar(toolbar)
        displayHomeAsUp(true)
    }
}