package com.example.genst.view.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genst.data.genstresponse.GetNotificationResponse
import com.example.genst.data.preference.Repository
import com.example.genst.data.result.Results
import kotlinx.coroutines.launch

class NotificationViewModel (private val  repository: Repository) : ViewModel() {
    private val _notification = MutableLiveData<Results<GetNotificationResponse>>()
    val notification: LiveData<Results<GetNotificationResponse>> = _notification

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
}