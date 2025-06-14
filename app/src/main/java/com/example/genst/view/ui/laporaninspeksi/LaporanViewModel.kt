package com.example.genst.view.ui.laporaninspeksi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genst.data.genstresponse.GetReportDetailResponse
import com.example.genst.data.preference.Repository
import com.example.genst.data.result.Results
import kotlinx.coroutines.launch

class LaporanViewModel (private val repository: Repository) : ViewModel() {
    private val _reportDetail = MutableLiveData<Results<GetReportDetailResponse>> ()
    val getreportDetailResponse: LiveData<Results<GetReportDetailResponse>> = _reportDetail

    fun fetchReportDetail(token: String, id: Int) {
        viewModelScope.launch {
            repository.getReportDetail(token, id).observeForever { result ->
                _reportDetail.value = result
            }
        }
    }
}