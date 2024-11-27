package com.example.notesappwithhilt.commonUtils

import android.content.Context
import com.example.notesappwithhilt.models.LoginData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrefManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPref = context.getSharedPreferences(KeyConstants.PREF_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    var accessToken: String?
        get() = sharedPref.getString("accessToken", "") // Ensure consistent naming
        set(token) {
            editor.putString("accessToken", token).apply()
        }

    var isLoggedIn: Boolean
        get() = sharedPref.getBoolean(Enums.IsLoggedIn.toString(), false)
        set(value) {
            editor.putBoolean(Enums.IsLoggedIn.toString(), value).apply()
        }

    var logginUserData: LoginData?
        get() {
            val json = sharedPref.getString(Enums.LoginUserData.toString(), "")
            val type = object : TypeToken<LoginData?>() {}.type
            return Gson().fromJson(json, type)
        }
        set(value) {
            val json = Gson().toJson(value)
            editor.putString(Enums.LoginUserData.toString(), json).apply()
        }

    fun clearPreferences() {
        editor.clear().apply()
    }

    companion object {
        fun get(context: Context) = PrefManager(context)
    }
}
