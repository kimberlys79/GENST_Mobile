package com.example.genst.data.genstresponse

import com.google.gson.annotations.SerializedName

data class GeneratorItem(

	@field:SerializedName("generator_code")
	val generatorCode: String? = null,

	@field:SerializedName("generator_name")
	val generatorName: String? = null,

	@field:SerializedName("power")
	val power: String? = null,

	@field:SerializedName("generator_id")
	val generatorId: Int? = null
)

data class GetGeneratorResponse(

	@field:SerializedName("generator")
	val generator: List<GeneratorItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
