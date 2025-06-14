package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class GeneratorDetailItem(

	@field:SerializedName("generator_code")
	val generatorCode: String? = null,

	@field:SerializedName("generator_name")
	val generatorName: String? = null,

	@field:SerializedName("power")
	val power: String? = null,

	@field:SerializedName("generator_id")
	val generatorId: Int? = null
)

data class GetGeneratorDetailResponse(

	@field:SerializedName("generatorDetail")
	val generatorDetail: List<GeneratorDetailItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
