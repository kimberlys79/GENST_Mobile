package com.example.genst.data.preference

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val user = UserModel(
                preferences[ID_KEY] ?: 0,
                preferences[TOKEN_KEY] ?: "",
                preferences[BADGE_NUMBER] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
            )
            Log.d("UserPreference", "getSession emit: $user")
            user
        }
    }

    suspend fun saveUser(id: Int?, token: String?, email: String?, badgeNumber: String?) {
        if (id != null && token != null && email != null && badgeNumber != null) {
            dataStore.edit { preferences ->
                preferences[ID_KEY] = id
                preferences[TOKEN_KEY] = token
                preferences[BADGE_NUMBER] = badgeNumber
                preferences[EMAIL_KEY] = email
                preferences[IS_LOGIN_KEY] = true
            }
            Log.d("UserPreference", "saveUser: id=$id token=$token email=$email badgeNumber=$badgeNumber isLogin=true")
        } else {
            Log.e("UserPreference", "saveUser called with NULL values → save aborted!")
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = intPreferencesKey("id")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val BADGE_NUMBER = stringPreferencesKey("badge_number")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}