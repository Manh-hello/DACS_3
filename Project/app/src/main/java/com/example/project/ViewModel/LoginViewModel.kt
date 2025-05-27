package com.example.project.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.Customer
import com.example.project.Repository.Customers
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginSuccess = MutableLiveData<String>()
    val loginSuccess: LiveData<String> = _loginSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val customers = Customers()

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Vui lòng nhập đầy đủ email và mật khẩu"
            return
        }
        viewModelScope.launch {
            try {
                val admin = customers.login(email)
                if (admin != null && admin.password == password) {
                    _loginSuccess.value = admin.id
                } else {
                    _errorMessage.value = "Sai email hoặc mật khẩu"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Đã xảy ra lỗi: ${e.message}"
                Log.d("error", e.message.toString())
            }
        }
    }
}
