package bm.babimumba.diabete.utils

import android.content.Context
import android.content.SharedPreferences

object RoleManager {
    private const val PREF_NAME = "UserRolePrefs"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_USER_ID = "user_id"
    
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveUserRole(context: Context, role: String, userId: String) {
        val prefs = getSharedPreferences(context)
        prefs.edit()
            .putString(KEY_USER_ROLE, role)
            .putString(KEY_USER_ID, userId)
            .apply()
    }
    
    fun getUserRole(context: Context): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(KEY_USER_ROLE, null)
    }
    
    fun getUserId(context: Context): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(KEY_USER_ID, null)
    }
    
    fun clearUserData(context: Context) {
        val prefs = getSharedPreferences(context)
        prefs.edit().clear().apply()
    }
    
    fun isMedecin(context: Context): Boolean {
        return getUserRole(context) == "medecin"
    }
    
    fun isPatient(context: Context): Boolean {
        return getUserRole(context) == "patient"
    }
} 