package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class UpdatedUser(

	@field:SerializedName("name")
	val name: String? = null
)

data class UpdateUserResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("updatedUser")
	val updatedUser: UpdatedUser? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
