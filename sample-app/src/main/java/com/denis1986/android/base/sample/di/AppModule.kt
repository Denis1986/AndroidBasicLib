package com.denis1986.android.base.sample.di

import android.app.Application
import android.content.Context
import com.denis1986.android.base.network.NetworkStateManager
import com.denis1986.android.base.sample.base.RetryPolicy
import com.denis1986.android.base.sample.service.AppPreferences
import com.denis1986.android.base.sample.service.FilePathProvider
import com.denis1986.android.base.util.AppExecutors
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

/**
 * Created by Denis Druzhinin on 02.01.2021.
 */
@Module
class AppModule(private val application: Application) {

    private val context = application

    @Provides
    fun provideContext(): Context = context

    @Provides
    fun provideApplication(): Application = application

    @Singleton
    @Provides
    fun provideAppPreferences(): AppPreferences = AppPreferences(context)

    @Singleton
    @Provides
    fun provideExecutors(): AppExecutors = AppExecutors()

    @Provides
    fun provideFilePathProvider(): FilePathProvider = FilePathProvider(context)

    @Provides
    @Singleton
    fun provideNetworkStateManager(): NetworkStateManager = NetworkStateManager(context)

    @Provides
    @Reusable
    fun provideRetryPolicy(): RetryPolicy = RetryPolicy()
}