package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class UserDetailItem(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("badge_number")
	val badgeNumber: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null
)

data class GetUserDetailResponse(

	@field:SerializedName("userDetail")
	val userDetail: List<UserDetailItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
