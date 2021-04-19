package io.github.denis1986.android.base.sample.screens.persistent

import android.app.Application
import androidx.lifecycle.viewModelScope
import io.github.denis1986.android.base.io.FileSaver
import io.github.denis1986.android.base.io.PersistentProperty
import io.github.denis1986.android.base.mvvm.MutableLiveDataWrapper
import io.github.denis1986.android.base.sample.base.SampleBasicViewModel
import io.github.denis1986.android.base.sample.model.User
import io.github.denis1986.android.base.sample.service.UserGenerator
import io.github.denis1986.android.base.sample.utils.CommonExtensions.nullToEmpty
import kotlinx.coroutines.launch

/**
 * Created by Denis Druzhinin on 29.12.2020.
 */
class PersistentPropertiesScreenViewModel(app: Application) : SampleBasicViewModel(app) {

    private val preferences = getAppComponent().provideAppPreferences()
    private val executors = getAppComponent().provideAppExecutors()
    private val filePathProvider = getAppComponent().provideFilePathProvider()

    private val userPropertyInPreferences = PersistentProperty(
        preferences.getUserSaver(),
        executors.diskIODispatcher
    ) { UserGenerator.generateEmptyUser() }

    private val userPropertyInFile = PersistentProperty(
        FileSaver(filePathProvider.getCurrentUserFilePath(), User::class.java),
        executors.diskIODispatcher
    ) { UserGenerator.generateEmptyUser() }

    val userFromPreferences = MutableLiveDataWrapper<User>()
    val userFromFile = MutableLiveDataWrapper<User>()

    init {
        viewModelScope.launch {
            userFromPreferences.asMutable().value = userPropertyInPreferences.getAsync()
            userFromFile.asMutable().value = userPropertyInFile.getAsync()
        }
    }

    fun onReloadFilePropertyButtonClicked() {
        viewModelScope.launch {
            userFromFile.asMutable().value = userPropertyInFile.getAsync()
        }
    }

    fun onUpdateFilePropertyButtonClicked(newValue: String?) {
        viewModelScope.launch {
            userPropertyInFile.updateAsync { user ->
                // We can be sure, that userPropertyInFile's value is already set, because we provided initializer, which creates new User instance if value is not set yet.
                user!!.name = newValue.nullToEmpty()
            }
        }
    }

    fun onReloadPreferencesPropertyButtonClicked() {
        viewModelScope.launch {
            userFromPreferences.asMutable().value = userPropertyInPreferences.getAsync()
        }
    }

    fun onUpdatePreferencesPropertyButton(newValue: String?) {
        viewModelScope.launch {
            userPropertyInPreferences.updateAsync { user ->
                // We can be sure, that userPropertyInPreferences's value is already set, because we provided initializer, which creates new User instance if value is not set yet.
                user!!.name = newValue.nullToEmpty()
            }
        }
    }
}