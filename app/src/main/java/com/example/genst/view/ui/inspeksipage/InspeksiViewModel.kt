package com.example.genst.view.ui.inspeksipage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genst.data.genstresponse.CreateReportResponse
import com.example.genst.data.preference.Repository
import com.example.genst.data.result.Results
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class InspeksiViewModel(private val repository: Repository) : ViewModel() {
    private val _createReport = MutableLiveData<Results<CreateReportResponse>>()
    val createReport: LiveData<Results<CreateReportResponse>> = _createReport

    fun createReport(
        lockEngine: RequestBody,
        lockEngineKeterangan: RequestBody,
        circuitBreaker: RequestBody,
        circuitBreakerKeterangan: RequestBody,
        fuelGenerator: RequestBody,
        oilGeneratorLevel: RequestBody,
        oilGeneratorLevelKeterangan: RequestBody,
        oilGeneratorColor: RequestBody,
        oilGeneratorColorKeterangan: RequestBody,
        radiatorWater: RequestBody,
        fuelPump: RequestBody,
        voltmeterBR: RequestBody,
        voltmeterYB: RequestBody,
        voltmeterRY: RequestBody,
        voltmeterRN: RequestBody,
        voltmeterYN: RequestBody,
        voltmeterBN: RequestBody,
        voltmeterKeterangan: RequestBody,
        ammeterBR: RequestBody,
        ammeterYB: RequestBody,
        ammeterRY: RequestBody,
        ammeterRN: RequestBody,
        ammeterYN: RequestBody,
        ammeterBN: RequestBody,
        ammeterKeterangan: RequestBody,
        batteryCharger: RequestBody,
        batteryChargerBR: RequestBody,
        batteryChargerYB: RequestBody,
        batteryChargerRY: RequestBody,
        batteryChargerRN: RequestBody,
        batteryChargerYN: RequestBody,
        batteryChargerBN: RequestBody,
        batteryChargerKeterangan: RequestBody,
        dutySelectorBR: RequestBody,
        dutySelectorYB: RequestBody,
        dutySelectorRY: RequestBody,
        dutySelectorRN: RequestBody,
        dutySelectorYN: RequestBody,
        dutySelectorBN: RequestBody,
        dutySelectorKeterangan: RequestBody,
        ecuCondition: RequestBody,
        ecuDescription: RequestBody,
        battery: RequestBody,
        batteryKeterangan: RequestBody,
        uploadPhoto: MultipartBody.Part,
        overallCondition: RequestBody,
        generatorSafeToOperate: RequestBody,
        inspectorSign: MultipartBody.Part,
        weekMaintenanceByMem: RequestBody,
        reportPdf: MultipartBody.Part,
        fkGeneratorReportId: Int,
    ) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    user.id.let { userId ->
                        _createReport.value = Results.Loading
                        try {
                            val result = repository.createReport(
                                token,
                                lockEngine,
                                lockEngineKeterangan,
                                circuitBreaker,
                                circuitBreakerKeterangan,
                                fuelGenerator,
                                oilGeneratorLevel,
                                oilGeneratorLevelKeterangan,
                                oilGeneratorColor,
                                oilGeneratorColorKeterangan,
                                radiatorWater,
                                fuelPump,
                                voltmeterBR,
                                voltmeterYB,
                                voltmeterRY,
                                voltmeterRN,
                                voltmeterYN,
                                voltmeterBN,
                                voltmeterKeterangan,
                                ammeterBR,
                                ammeterYB,
                                ammeterRY,
                                ammeterRN,
                                ammeterYN,
                                ammeterBN,
                                ammeterKeterangan,
                                batteryCharger,
                                batteryChargerBR,
                                batteryChargerYB,
                                batteryChargerRY,
                                batteryChargerRN,
                                batteryChargerYN,
                                batteryChargerBN,
                                batteryChargerKeterangan,
                                dutySelectorBR,
                                dutySelectorYB,
                                dutySelectorRY,
                                dutySelectorRN,
                                dutySelectorYN,
                                dutySelectorBN,
                                dutySelectorKeterangan,
                                ecuCondition,
                                ecuDescription,
                                battery,
                                batteryKeterangan,
                                uploadPhoto,
                                overallCondition,
                                generatorSafeToOperate,
                                inspectorSign,
                                weekMaintenanceByMem,
                                reportPdf,
                                userId,
                                fkGeneratorReportId
                            )
                            _createReport.value = result
                        } catch (e: Exception) {
                            _createReport.value = Results.Error(e.message.toString())
                        }
                    }
                }
            }
        }
    }
}