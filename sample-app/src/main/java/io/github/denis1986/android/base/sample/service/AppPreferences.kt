package io.github.denis1986.android.base.sample.service

import android.content.Context
import android.preference.PreferenceManager
import io.github.denis1986.android.base.io.BasicPreferences
import io.github.denis1986.android.base.io.PreferencesObjectSaver
import io.github.denis1986.android.base.sample.model.User
import com.google.gson.Gson

/**
 * Created by Denis Druzhinin on 02.01.2021.
 */
class AppPreferences(context: Context): BasicPreferences(PreferenceManager.getDefaultSharedPreferences(context)) {

    private val gson = Gson()

    fun getUserSaver(): PreferencesObjectSaver<User> {
        return PreferencesObjectSaver(KEY_CURRENT_USER, getType<User>(), this, gson)
    }

    companion object {
        const val KEY_CURRENT_USER = "sample:currentUser"
    }
}