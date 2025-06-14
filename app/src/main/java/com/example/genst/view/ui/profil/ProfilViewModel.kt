package com.example.genst.view.ui.profil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.genst.data.genstresponse.GetUserDetailResponse
import com.example.genst.data.preference.Repository
import com.example.genst.data.result.Results
import com.example.genst.injection.Data
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfilViewModel(private val repository: Repository) : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    private val _userDetailResult = MediatorLiveData<Results<GetUserDetailResponse>>()
    val userDetailResponse: LiveData<Results<GetUserDetailResponse>> = _userDetailResult

    fun getUserDetail() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                if (user.token.isNotEmpty() && user.id != 0 && user.isLogin) {
                    val result = repository.getUserDetail(user.token, user.id)
                    _userDetailResult.addSource(result) { resultData ->
                        _userDetailResult.value = resultData
                    }
                } else {
                    // Jika token kosong, jangan panggil API, clear UI secara aman
                    _userDetailResult.value = Results.Error("User not logged in")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
