package com.example.genst.view.ui.authentication

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genst.data.genstresponse.CreateUserResponse
import com.example.genst.data.genstresponse.LoginUserResponse
import com.example.genst.data.preference.Repository
import com.example.genst.data.result.Results
import kotlinx.coroutines.launch

class AuthenticationViewModel (private val repository: Repository) : ViewModel() {
    private val _loginResponse = MediatorLiveData<Results<LoginUserResponse>>()
    val loginUserResponse: MediatorLiveData<Results<LoginUserResponse>> = _loginResponse

    private val _registerResponse = MediatorLiveData<Results<CreateUserResponse>>()
    val registerUserResponse: MediatorLiveData<Results<CreateUserResponse>> = _registerResponse

    fun login(badgeNumber: String, email: String, password: String) {
        viewModelScope.launch {
            _loginResponse.value = Results.Loading
            try {
                val response = repository.login(badgeNumber, email, password)
                _loginResponse.value = Results.Success(response)
            } catch (e: Exception) {
                _loginResponse.value = Results.Error(e.message.toString())
            }
        }
    }

    fun createNewUser(name: String, badgeNumber: String, email: String, password: String)
    {
        viewModelScope.launch {
            _registerResponse.value = Results.Loading
            try {
                val response =
                    repository.createNewUser(name, badgeNumber, email, password)
                _registerResponse.value = Results.Success(response)
            } catch (e: Exception) {
                _registerResponse.value = Results.Error(e.message.toString())
            }
        }
    }

}