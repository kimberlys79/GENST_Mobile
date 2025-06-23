package com.example.genst.view.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genst.data.genstresponse.CreateNotificationResponse
import com.example.genst.data.genstresponse.GetNotificationResponse
import com.example.genst.data.preference.Repository
import com.example.genst.data.result.Results
import kotlinx.coroutines.launch

class NotificationViewModel (private val repository: Repository) : ViewModel() {

    // Untuk getAllNotification
    private val _notification = MediatorLiveData<Results<GetNotificationResponse>>()
    val notification: LiveData<Results<GetNotificationResponse>> = _notification

    // Untuk createNotification
    private val _createNotification = MediatorLiveData<Results<CreateNotificationResponse>>()
    val createNotification: LiveData<Results<CreateNotificationResponse>> = _createNotification

    // Ini untuk menampilkan semua notifikasi (GET)
    fun fetchNotifications() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                val token = user.token
                repository.getAllNotification(token).observeForever { result ->
                    _notification.value = result
                }
            }
        }
    }

    // Ini untuk mengirim notifikasi baru (POST)
    fun sendNotification(title: String, message: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                val token = user.token
                _createNotification.value = Results.Loading
                try {
                    val result = repository.createNotification(token, title, message)
                    result.observeForever { response ->
                        _createNotification.value = response
                    }
                } catch (e: Exception) {
                    _createNotification.value = Results.Error(e.message.toString())
                }
            }
        }
    }
}