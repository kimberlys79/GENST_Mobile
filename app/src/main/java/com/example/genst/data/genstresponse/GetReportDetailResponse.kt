package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class InspectorSign(

	@field:SerializedName("data")
	val data: List<Int?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class UploadPhoto(

	@field:SerializedName("data")
	val data: List<Int?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class GetReportDetailResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("reportDetail")
	val reportDetail: List<ReportDetailItem?>? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class ReportDetailItem(

	@field:SerializedName("inspector_sign")
	val inspectorSign: InspectorSign? = null,

	@field:SerializedName("generator_safe_to_operate")
	val generatorSafeToOperate: String? = null,

	@field:SerializedName("radiator_water")
	val radiatorWater: String? = null,

	@field:SerializedName("date_time")
	val dateTime: String? = null,

	@field:SerializedName("report_id")
	val reportId: Int? = null,

	@field:SerializedName("fuel_generator")
	val fuelGenerator: String? = null,

	@field:SerializedName("fk_user_report_id")
	val fkUserReportId: Int? = null,

	@field:SerializedName("fk_generator_report_id")
	val fkGeneratorReportId: Int? = null,

	@field:SerializedName("upload_photo")
	val uploadPhoto: UploadPhoto? = null,

	@field:SerializedName("fuel_pump")
	val fuelPump: String? = null,

	@field:SerializedName("overall_condition")
	val overallCondition: String? = null,

	@field:SerializedName("week_maintenance_by_mem")
	val weekMaintenanceByMem: Int? = null
)
