package com.denis1986.android.base.sample.di

import com.denis1986.android.base.network.ConnectionManager
import com.denis1986.android.base.sample.base.RetryPolicy
import com.denis1986.android.base.sample.service.AppPreferences
import com.denis1986.android.base.sample.service.FilePathProvider
import com.denis1986.android.base.util.AppExecutors
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Denis Druzhinin on 02.01.2021.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun provideAppPreferences(): AppPreferences
    fun provideAppExecutors(): AppExecutors
    fun provideFilePathProvider(): FilePathProvider
    fun provideConnectionManager(): ConnectionManager
    fun provideRetryPolicy(): RetryPolicy
}