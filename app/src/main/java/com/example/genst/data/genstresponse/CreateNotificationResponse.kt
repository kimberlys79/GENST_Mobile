package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class CreateNotificationResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("newNotification")
	val newNotification: NewNotification? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class NewNotification(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
