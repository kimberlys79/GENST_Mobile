package com.example.genst.data.retrofit

import com.example.genst.data.genstresponse.CreateReportResponse
import com.example.genst.data.genstresponse.CreateUserResponse
import com.example.genst.data.genstresponse.CreateNotificationResponse
import com.example.genst.data.genstresponse.DeleteUserResponse
import com.example.genst.data.genstresponse.GetGeneratorDetailResponse
import com.example.genst.data.genstresponse.GetGeneratorResponse
import com.example.genst.data.genstresponse.GetNotificationResponse
import com.example.genst.data.genstresponse.GetReportDetailResponse
import com.example.genst.data.genstresponse.GetReportResponse
import com.example.genst.data.genstresponse.GetUserDetailResponse
import com.example.genst.data.genstresponse.GetUserResponse
import com.example.genst.data.genstresponse.LoginUserResponse
import com.example.genst.data.genstresponse.UpdateUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

//USER
interface ServiceApi {
    @FormUrlEncoded
    @POST("user/add")
    suspend fun createNewUser(
        @Field("name") name: String,
        @Field("badge_number") badgeNumber: String,
        @Field("email") email: String,
        @Field("password") password: String,
        ): CreateUserResponse

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("badge_number") badgeNumber: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginUserResponse

    @GET("user/")
    suspend fun getAllUser(
        @Header("Authorization") token: String,
    ): GetUserResponse

    @GET("user/{id}")
    suspend fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): GetUserDetailResponse

    @FormUrlEncoded
    @PATCH("user/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("badge_number") badgeNumber: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): UpdateUserResponse

    @DELETE("user/{id}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): DeleteUserResponse

    //NOTIFICATION
    @FormUrlEncoded
    @POST("notification/add")
    suspend fun createNotification(
        @Header("Authorization") token: String,
        @Field("title") title: String,
        @Field("message") message: String,
    ): CreateNotificationResponse

    @GET("notification/")
    suspend fun getAllNotification(
        @Header("Authorization") token: String,
    ): GetNotificationResponse

    //GENERATOR
    @GET("generator/")
    suspend fun getAllGenerator(
        @Header("Authorization") token: String,
    ): GetGeneratorResponse

    @GET("generator/{id}")
    suspend fun getGeneratorDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): GetGeneratorDetailResponse

    //REPORT
    @Multipart
    @POST("report/add")
    suspend fun createReport(
        @Header("Authorization") token: String,
        @Part("lock_engine_condition") lockEngine: RequestBody,
        @Part("lock_engine_description") lockEngineKeterangan: RequestBody,
        @Part("circuit_breaker_condition") circuitBreaker: RequestBody,
        @Part("circuit_breaker_description") circuitBreakerKeterangan: RequestBody,
        @Part("fuel_generator") fuelGenerator: RequestBody,
        @Part("oil_generator_level_condition") oilGeneratorLevel: RequestBody,
        @Part("oil_generator_level_description") oilGeneratorLevelKeterangan: RequestBody,
        @Part("oil_generator_color_condition") oilGeneratorColor: RequestBody,
        @Part("oil_generator_color_description") oilGeneratorColorKeterangan: RequestBody,
        @Part("radiator_water") radiatorWater: RequestBody,
        @Part("fuel_pump") fuelPump: RequestBody,
        @Part("voltmeter_br") voltmeterBR: RequestBody,
        @Part("voltmeter_yb") voltmeterYB: RequestBody,
        @Part("voltmeter_ry") voltmeterRY: RequestBody,
        @Part("voltmeter_rn") voltmeterRN: RequestBody,
        @Part("voltmeter_yn") voltmeterYN: RequestBody,
        @Part("voltmeter_bn") voltmeterBN: RequestBody,
        @Part("voltmeter_description") voltmeterKeterangan: RequestBody,
        @Part("ammeter_br") ammeterBR: RequestBody,
        @Part("ammeter_yb") ammeterYB: RequestBody,
        @Part("ammeter_ry") ammeterRY: RequestBody,
        @Part("ammeter_rn") ammeterRN: RequestBody,
        @Part("ammeter_yn") ammeterYN: RequestBody,
        @Part("ammeter_bn") ammeterBN: RequestBody,
        @Part("ammeter_description") ammeterKeterangan: RequestBody,
        @Part("battery_charger_condition") batteryCharger: RequestBody,
        @Part("battery_charger_br") batteryChargerBR: RequestBody,
        @Part("battery_charger_yb") batteryChargerYB: RequestBody,
        @Part("battery_charger_ry") batteryChargerRY: RequestBody,
        @Part("battery_charger_rn") batteryChargerRN: RequestBody,
        @Part("battery_charger_yn") batteryChargerYN: RequestBody,
        @Part("battery_charger_bn") batteryChargerBN: RequestBody,
        @Part("battery_charger_description") batteryChargerKeterangan: RequestBody,
        @Part("duty_selector_br") dutySelectorBR: RequestBody,
        @Part("duty_selector_yb") dutySelectorYB: RequestBody,
        @Part("duty_selector_ry") dutySelectorRY: RequestBody,
        @Part("duty_selector_rn") dutySelectorRN: RequestBody,
        @Part("duty_selector_yn") dutySelectorYN: RequestBody,
        @Part("duty_selector_bn") dutySelectorBN: RequestBody,
        @Part("duty_selector_description") dutySelectorKeterangan: RequestBody,
        @Part("ecu_condition") ecuCondition: RequestBody,
        @Part("ecu_description") ecuDescription: RequestBody,
        @Part("battery_condition") battery: RequestBody,
        @Part("battery_description") batteryKeterangan: RequestBody,
        @Part uploadPhoto: MultipartBody.Part,
        @Part("overall_condition") overallCondition: RequestBody,
        @Part("generator_safe_to_operate") generateSafeToOperate: RequestBody,
        @Part inspectorSign: MultipartBody.Part,
        @Part("week_maintenance_by_mem") weekMaintenanceByMem: RequestBody,
        @Part reportPdf: MultipartBody.Part,
        @Part("fk_user_report_id") fkUserReportId: Int,
        @Part("fk_generator_report_id") fkGeneratorReportId: Int,
    ): CreateReportResponse

    @GET("report/")
    suspend fun getAllReport(
        @Header("Authorization") token: String,
    ): GetReportResponse

    @GET("report/{id}")
    suspend fun getReportDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): GetReportDetailResponse
}




