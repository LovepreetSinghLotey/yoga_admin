package com.example.universal_yoga_admin.preferences

import android.content.SharedPreferences
import javax.inject.Inject

enum class PrefKeys(val value: String) {
    USER_ID("userId")
}

class PreferenceUtil @Inject constructor(
    private val prefs: SharedPreferences,
) {

    fun saveUserId(userId: String){
        prefs.edit().putString(PrefKeys.USER_ID.value, userId).apply()
    }

    /**
    * If user id is present that means user is logged in otherwise not
    */
    fun getUserId(): String{
        return prefs.getString(PrefKeys.USER_ID.value, "") ?: ""
    }

    fun clearAllPrefs(){
        prefs.edit().clear().apply()
    }

}