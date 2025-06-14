package com.example.genst.view.ui.riwayatlaporan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genst.data.genstresponse.GetReportResponse
import com.example.genst.data.preference.Repository
import com.example.genst.data.result.Results
import kotlinx.coroutines.launch

class RiwayatLaporanViewModel(private val repository: Repository) : ViewModel() {

    private val _allReport = MediatorLiveData<Results<GetReportResponse>>()
    val allReport: LiveData<Results<GetReportResponse>> = _allReport

    fun fetchAllReport() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val response = repository.getAllReport(token)
                    _allReport.addSource(response) { allReportResult ->
                        _allReport.value = allReportResult
                    }
                }
            }
        }
    }
}

