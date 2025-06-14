package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class DeleteUserResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
