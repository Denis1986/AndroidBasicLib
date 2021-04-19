package io.github.denis1986.android.base.io

import android.content.SharedPreferences
import io.github.denis1986.android.base.util.ThreadUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.lang.reflect.Type

/** Basic class for AppPreferences, which gives an ability to save not only primitives, but also complex data structures with the help of Gson.
 *
 * Created by denis.druzhinin on 21.06.2019.
 */
abstract class BasicPreferences(private val preferences: SharedPreferences) {

    protected fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

    protected  fun getNullableBoolean(key: String): Boolean? {
        return if (preferences.contains(key)) getBoolean(key, false) else null
    }

    protected fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    protected fun putBoolean(key: String, value: Boolean, saveType: SaveType) {
        val editor = preferences.edit().putBoolean(key, value)
        save(editor, saveType)
    }

    protected fun putBoolean(key: String, value: Boolean) {
        putBoolean(key, value, SaveType.Apply)
    }

    protected fun getNullableLong(key: String): Long? {
        return if (preferences.contains(key)) getLong(key) else null
    }

    protected fun getLong(key: String): Long {
        return preferences.getLong(key, 0)
    }

    protected fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    protected fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    protected fun putInt(key: String, value: Int) {
        val editor = preferences.edit().putInt(key, value)
        save(editor, SaveType.Apply)
    }

    protected fun putString(key: String, value: String?) {
        if (value == null) remove(key) else putString(key, value, SaveType.Apply)
    }

    protected fun putOrRemoveString(key: String, value: String?) {
        if (value == null) {
            remove(key)
        } else {
            putString(key, value, SaveType.Apply)
        }
    }

    protected fun putString(key: String, value: String, saveType: SaveType) {
        val editor = preferences.edit().putString(key, value)
        save(editor, saveType)
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    protected fun putStringList(key: String, list: List<String>?) {
        if (list == null) {
            remove(key)
        } else {
            val appTokensJson = JSONArray(list)
            putString(key, appTokensJson.toString())
        }
    }

    protected fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun putObjectJson(gson: Gson, key: String, obj: Any?) {
        if (obj == null) {
            remove(key)
        } else {
            val dataJson = gson.toJson(obj)
            putString(key, dataJson)
        }
    }

    private fun save(editor: SharedPreferences.Editor, saveType: SaveType) {
        // There were examples of ANRs, caused by commit() method call, hence call only apply() on UI thread.
        if (saveType == SaveType.Commit && !ThreadUtils.getInstance().isUiThread()) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    enum class SaveType {
        Commit,
        Apply
    }

    companion object {
        inline fun <reified T> getType(): Type {
            return object: TypeToken<T>() { }.type
        }
    }
}