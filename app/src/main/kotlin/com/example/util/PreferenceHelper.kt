package com.example.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    companion object {
        private const val prefsFileName = "com.example.prefs"
        private const val jwtTokenConst = "jwt_token"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFileName, MODE_PRIVATE)

    var jwtToken: String?
        get() = prefs.getString(jwtTokenConst, "")
        set(value) = prefs.edit().putString(jwtTokenConst, value).apply()
}
