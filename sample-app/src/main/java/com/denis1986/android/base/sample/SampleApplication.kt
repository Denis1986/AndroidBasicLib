package com.denis1986.android.base.sample

import android.app.Application
import com.denis1986.android.base.sample.di.AppComponent
import com.denis1986.android.base.sample.di.AppModule
import com.denis1986.android.base.sample.di.DaggerAppComponent

/**
 * Created by Denis Druzhinin on 02.01.2021.
 */
class SampleApplication : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = createAppComponent()
    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        lateinit var instance: SampleApplication
            private set
    }
}