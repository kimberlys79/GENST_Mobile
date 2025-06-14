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
        @Part("lock_engine") lockEngine: String,
        @Part("lock_engine_keterangan") lockEngineKeterangan: String,
        @Part("circuit_breaker") circuitBreaker: String,
        @Part("circuit_breaker_keterangan") circuitBreakerKeterangan: String,
        @Part("fuel_generator") fuelGenerator: String,
        @Part("oil_generator_level") oilGeneratorLevel: String,
        @Part("oil_generator_level_keterangan") oilGeneratorLevelKeterangan: String,
        @Part("oil_generator_color") oilGeneratorColor: String,
        @Part("oil_generator_color_keterangan") oilGeneratorColorKeterangan: String,
        @Part("radiator_water") radiatorWater: String,
        @Part("fuel_pump") fuelPump: String,
        @Part("voltmeter_br") voltmeterBR: Int,
        @Part("voltmeter_yb") voltmeterYB: Int,
        @Part("voltmeter_ry") voltmeterRY: Int,
        @Part("voltmeter_rn") voltmeterRN: Int,
        @Part("voltmeter_yn") voltmeterYN: Int,
        @Part("voltmeter_bn") voltmeterBN: Int,
        @Part("voltmeter_keterangan") voltmeterKeterangan: String,
        @Part("ammeter_br") ammeterBR: Int,
        @Part("ammeter_yb") ammeterYB: Int,
        @Part("ammeter_ry") ammeterRY: Int,
        @Part("ammeter_rn") ammeterRN: Int,
        @Part("ammeter_yn") ammeterYN: Int,
        @Part("ammeter_bn") ammeterBN: Int,
        @Part("ammeter_keterangan") ammeterKeterangan: String,
        @Part("battery_charger") batteryCharger: String,
        @Part("battery_charger_br") batteryChargerBR: Int,
        @Part("battery_charger_yb") batteryChargerYB: Int,
        @Part("battery_charger_ry") batteryChargerRY: Int,
        @Part("battery_charger_rn") batteryChargerRN: Int,
        @Part("battery_charger_yn") batteryChargerYN: Int,
        @Part("battery_charger_bn") batteryChargerBN: Int,
        @Part("battery_charger_keterangan") batteryChargerKeterangan: String,
        @Part("ecu") ecu: String,
        @Part("battery") battery: String,
        @Part("battery_keterangan") batteryKeterangan: String,
        @Part uploadPhoto: String,
        @Part("overall_condition") overallCondition: String,
        @Part("generator_safe_to_operate") generateSafeToOperate: String,
        @Part ("inspectorSign") inspectorSign: String,
        @Part("week_maintenance_by_mem") weekMaintenanceByMem: String,
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




