package com.example.genst.data.genstresponse

import android.health.connect.datatypes.units.Power
import com.google.gson.annotations.SerializedName

data class ReportItem(

	@field:SerializedName("inspector_name")
	val inspectorName: String? = null,

	@field:SerializedName("generator_code")
	val generatorCode: String? = null,

	@field:SerializedName("generator_type")
	val generatorType: String? = null,

	@field:SerializedName("report_date")
	val reportDate: String? = null,
)

data class GetReportResponse(

	@field:SerializedName("report")
	val report: List<ReportItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
