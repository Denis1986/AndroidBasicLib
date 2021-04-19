package io.github.denis1986.android.base.sample.base

import android.app.Application
import io.github.denis1986.android.base.mvvm.component.BasicViewModel
import io.github.denis1986.android.base.sample.SampleApplication

/**
 * Created by Denis Druzhinin on 03.01.2021.
 */
open class SampleBasicViewModel(app: Application) : BasicViewModel(app) {

    protected fun getAppComponent() = SampleApplication.instance.appComponent
}