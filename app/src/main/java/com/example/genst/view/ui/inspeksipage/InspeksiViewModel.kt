package com.example.genst.view.ui.inspeksipage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genst.data.genstresponse.CreateReportResponse
import com.example.genst.data.preference.Repository
import com.example.genst.data.result.Results
import kotlinx.coroutines.launch

class InspeksiViewModel(private val repository: Repository) : ViewModel() {
    private val _createReport = MutableLiveData<Results<CreateReportResponse>>()
    val createReport: LiveData<Results<CreateReportResponse>> = _createReport

    fun createReport(
        lockEngine: String,
        lockEngineKeterangan: String,
        circuitBreaker: String,
        circuitBreakerKeterangan: String,
        fuelGenerator: String,
        oilGeneratorLevel: String,
        oilGeneratorLevelKeterangan: String,
        oilGeneratorColor: String,
        oilGeneratorColorKeterangan: String,
        radiatorWater: String,
        fuelPump: String,
        voltmeterBR: Int,
        voltmeterYB: Int,
        voltmeterRY: Int,
        voltmeterRN: Int,
        voltmeterYN: Int,
        voltmeterBN: Int,
        voltmeterKeterangan: String,
        ammeterBR: Int,
        ammeterYB: Int,
        ammeterRY: Int,
        ammeterRN: Int,
        ammeterYN: Int,
        ammeterBN: Int,
        ammeterKeterangan: String,
        batteryCharger: String,
        batteryChargerBR: Int,
        batteryChargerYB: Int,
        batteryChargerRY: Int,
        batteryChargerRN: Int,
        batteryChargerYN: Int,
        batteryChargerBN: Int,
        batteryChargerKeterangan: String,
        ECU: String,
        battery: String,
        batteryKeterangan: String,
        uploadPhoto: String,
        overallCondition: String,
        generatorSafeToOperate: String,
        inspectorSign: String,
        weekMaintenanceByMem: String,
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
                                ECU,
                                battery,
                                batteryKeterangan,
                                uploadPhoto,
                                overallCondition,
                                generatorSafeToOperate,
                                inspectorSign,
                                weekMaintenanceByMem,
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