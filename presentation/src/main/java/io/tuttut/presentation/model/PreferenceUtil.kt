package io.tuttut.presentation.model

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceUtil @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun getString(key: String, defValue: String? = ""): String? {
        return prefs.getString(key, defValue)
    }

    fun setString(key: String, value: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.putString(key, value).apply()
        }
    }

    fun clear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.clear().apply()
        }
    }

    companion object {
        private const val PREFERENCE = "preference"
        private const val GARDEN_ID = "gardenId"
    }

    var gardenId: String get() = getString(GARDEN_ID).toString()
        set(value) {
            setString(GARDEN_ID, value)
        }
}