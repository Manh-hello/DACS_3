package com.example.project.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.Notification
import com.example.project.R
import com.example.project.Repository.Notifications
import kotlinx.coroutines.launch

class BellViewModel() : ViewModel() {
    private val _notification = MutableLiveData<List<Notification>>()
    val notification: LiveData<List<Notification>> = _notification

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val notifications = Notifications()

    fun loadNotification(cusId: String) {
        viewModelScope.launch {
            try {
                _notification.value = notifications.selectAll(cusId).reversed()
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun save(cusId: String, type: String, title: String, mes: String, time: String){
        viewModelScope.launch {
            try {
                _success.value = notifications.save(cusId,type,title,mes,time)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
}