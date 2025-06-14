package com.example.genst.data.preference

import android.speech.tts.TextToSpeech.Engine
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.genst.data.genstresponse.CreateNotificationResponse
import com.example.genst.data.genstresponse.CreateReportResponse
import com.example.genst.data.genstresponse.DeleteUserResponse
import com.example.genst.data.genstresponse.GetGeneratorResponse
import com.example.genst.data.genstresponse.CreateUserResponse
import com.example.genst.data.genstresponse.GetNotificationResponse
import com.example.genst.data.genstresponse.GetReportDetailResponse
import com.example.genst.data.genstresponse.GetReportResponse
import com.example.genst.data.genstresponse.GetUserDetailResponse
import com.example.genst.data.genstresponse.GetUserResponse
import com.example.genst.data.genstresponse.LoginUserResponse
import com.example.genst.data.genstresponse.UpdateUserResponse
import com.example.genst.data.result.Results
import com.example.genst.data.retrofit.ServiceApi
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiServiceGENST: ServiceApi,
) {
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun createNewUser(
        name: String,
        badgeNumber: String,
        email: String,
        password: String,
    ): CreateUserResponse {
        return apiServiceGENST.createNewUser(name, badgeNumber, email, password)
    }

    suspend fun login(
        badgeNumber: String,
        email: String,
        password: String,
    ): LoginUserResponse {
        return try {
            val loginResponse = apiServiceGENST.login(badgeNumber, email, password)
            loginResponse.loginResult.let {
                userPreference.saveUser(
                    id = it?.id,
                    token = it?.token,
                    email = it?.email,
                    badgeNumber = it?.badgeNumber
                )
                Log.d("Repository", "Saved User in Session: ${it?.id}, token=${it?.token}, email=${it?.email}, badgeNumber=${it?.badgeNumber}")
            }
            Log.d("Repository", "Login Success")
            loginResponse
        } catch (e: HttpException) {
            Log.e("Repository", "HTTP Exception: ${e.message()}")
            throw Exception("Login failed due to server error: ${e.message()}")
        } catch (e: IOException) {
            Log.e("Repository", "IO Exception: ${e.message}")
            throw Exception("Network error, please check your connection.")
        } catch (e: Exception) {
            Log.e("Repository", "General Exception: ${e.message}")
            throw Exception("unexpected error occurred: ${e.message}")
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getAllUser(
        token: String,
        id: Int,
    ): LiveData<Results<GetUserResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceGENST.getAllUser("Bearer $token")
            Log.d("Repository", "Get All User Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get All User: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occured"))
        }
    }

    suspend fun getUserDetail(
        token: String,
        id: Int,
    ): LiveData<Results<GetUserDetailResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceGENST.getUserDetail("Bearer $token", id)
            Log.d("Repository", "Get User Detail Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error  Get User Detail: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occured"))
        }
    }

    suspend fun updateUser(
        token: String,
        id: Int,
        name: String,
        badgeNumber: String,
        email: String,
        password: String,
    ): UpdateUserResponse {
        Log.d("Repository", "Update User with ID $id")
        return apiServiceGENST.updateUser("Bearer $token", id, name, badgeNumber, email, password)
    }

    suspend fun deleteUser(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteUserResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceGENST.deleteUser("Bearer $token", id)
            Log.d("Repository", "Delete Client Response: $response")

        } catch (e: Exception) {
            Log.e("Repository", "Error Delete User: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occured"))
        }
    }

    suspend fun getAllNotification(
        token: String,
    ): LiveData<Results<GetNotificationResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceGENST.getAllNotification("Bearer $token")
            Log.d("Repository", "Get Notification Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Notification: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occured"))
        }
    }

    suspend fun createNotification(
        token: String,
        title: String,
        message: String,
    ): LiveData<Results<CreateNotificationResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceGENST.createNotification("Bearer $token", title, message)
            Log.d("Repository", "Create Notification Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Notification: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occured"))
        }
    }

    suspend fun getAllGenerator(
        token: String,
    ): LiveData<Results<GetGeneratorResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceGENST.getAllGenerator("Bearer $token")
            Log.d("Repository", "Get Generator Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Generator: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occured"))
        }
    }

    suspend fun getReportDetail(
        token: String,
        id: Int,
    ): LiveData<Results<GetReportDetailResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceGENST.getReportDetail("Bearer $token", id)
            Log.d("Repository", "Get Report Detail Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Report Detail: ${e.localizedMessage}", e)
            emit(Results.Error(e.message?: "Unknown error occured"))
        }
    }

    suspend fun getAllReport(
        token: String,
    ): LiveData<Results<GetReportResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceGENST.getAllReport("Bearer $token")
            Log.d("Repository", "Get All Report Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get All Report: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occured"))
        }
    }

    suspend fun createReport(
        token: String,
        lockEngine: String,
        lockEngineKeterangan: String,
        circuitBreaker: String,
        circuitBreakerKeterangan: String,
        fuelGenerator: String,
        oilGeneratorLevel: String,
        oilGeneratorLevelKeterangan: String,
        oilGeneratorColor: String,
        oilGeneratorColorKeterangan: String,
        radiatorWater: String,
        fuelPump: String,
        voltmeterBR: Int,
        voltmeterYB: Int,
        voltmeterRY: Int,
        voltmeterRN: Int,
        voltmeterYN: Int,
        voltmeterBN: Int,
        voltmeterKeterangan: String,
        ammeterBR: Int,
        ammeterYB: Int,
        ammeterRY: Int,
        ammeterRN: Int,
        ammeterYN: Int,
        ammeterBN: Int,
        ammeterKeterangan: String,
        batteryCharger: String,
        batteryChargerBR: Int,
        batteryChargerYB: Int,
        batteryChargerRY: Int,
        batteryChargerRN: Int,
        batteryChargerYN: Int,
        batteryChargerBN: Int,
        batteryChargerKeterangan: String,
        ECU: String,
        battery: String,
        batteryKeterangan: String,
        uploadPhoto: String,
        overallCondition: String,
        generatorSafeToOperate: String,
        inspectorSign: String,
        weekMaintenanceByMem: String,
        fkUserReportId: Int,
        fkGeneratorReportId: Int,
    ): Results<CreateReportResponse> {
        return try {
            val response = apiServiceGENST.createReport("Bearer $token",
                lockEngine, lockEngineKeterangan, circuitBreaker, circuitBreakerKeterangan,
                fuelGenerator, oilGeneratorLevel, oilGeneratorLevelKeterangan, oilGeneratorColor,
                oilGeneratorColorKeterangan, radiatorWater, fuelPump,
                voltmeterBR, voltmeterYB, voltmeterRY, voltmeterRN, voltmeterYN, voltmeterBN, voltmeterKeterangan,
                ammeterBR, ammeterYB, ammeterRY, ammeterRN, ammeterYN, ammeterBN, ammeterKeterangan,
                batteryCharger, batteryChargerBR, batteryChargerYB, batteryChargerRY, batteryChargerRN,
                batteryChargerYN, batteryChargerBN, batteryChargerKeterangan,
                ECU, battery, batteryKeterangan,
                uploadPhoto, overallCondition, generatorSafeToOperate, inspectorSign,
                weekMaintenanceByMem, fkUserReportId, fkGeneratorReportId)
            Results.Success(response)
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Report: ${e.localizedMessage}", e)
            Results.Error(e.message?: "Unknown error occured")
        }
    }

    companion object  {
        @Volatile
        private var instance:Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiServiceGENST: ServiceApi,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiServiceGENST)
            }.also { instance = it }
    }
}