package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class NotificationItem(

	@field:SerializedName("notification_id")
	val notificationId: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null
)

data class GetNotificationResponse(

	@field:SerializedName("notification")
	val notification: List<NotificationItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
