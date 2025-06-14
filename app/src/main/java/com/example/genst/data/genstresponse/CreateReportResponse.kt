package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class NewReport(

	@field:SerializedName("inspector_sign")
	val inspectorSign: String? = null,

	@field:SerializedName("generator_safe_to_operate")
	val generatorSafeToOperate: String? = null,

	@field:SerializedName("radiator_water")
	val radiatorWater: String? = null,

	@field:SerializedName("fuel_generator")
	val fuelGenerator: String? = null,

	@field:SerializedName("fk_user_report_id")
	val fkUserReportId: String? = null,

	@field:SerializedName("fk_generator_report_id")
	val fkGeneratorReportId: String? = null,

	@field:SerializedName("upload_photo")
	val uploadPhoto: String? = null,

	@field:SerializedName("fuel_pump")
	val fuelPump: String? = null,

	@field:SerializedName("overall_condition")
	val overallCondition: String? = null,

	@field:SerializedName("week_maintenance_by_mem")
	val weekMaintenanceByMem: String? = null
)

data class CreateReportResponse(

	@field:SerializedName("newReport")
	val newReport: NewReport? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
