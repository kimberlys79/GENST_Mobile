package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class LoginResult(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("badge_number")
    val badgeNumber: String? = null,

)

data class LoginUserResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null
)
