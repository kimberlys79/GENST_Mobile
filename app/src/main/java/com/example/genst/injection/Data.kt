package com.example.genst.injection

import android.content.Context
import android.util.Log
import com.example.genst.data.preference.Repository
import com.example.genst.data.preference.UserPreference
import com.example.genst.data.preference.dataStore
import com.example.genst.data.retrofit.ConfigApi

object Data {
    fun provideRepository(context: Context): Repository{
        try {
            val preference = UserPreference.getInstance(context.dataStore)
            val apiService = ConfigApi.getApiService()

            return Repository.getInstance(preference, apiService)
        } catch (e: Exception) {
            Log.e("Data", "Error in provideRepository", e)
            throw e
        }
    }
}