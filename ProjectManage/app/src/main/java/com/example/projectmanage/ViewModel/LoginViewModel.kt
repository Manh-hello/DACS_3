package com.example.projectmanage.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanage.Model.Entity.Admin
import com.example.projectmanage.Repository.Stores
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    private val stores = Stores()

    private val _loginSuccess = MutableLiveData<String>()
    val loginSuccess: LiveData<String> = _loginSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Vui lòng nhập đầy đủ email và mật khẩu"
            return
        }
        viewModelScope.launch {
            try {
                val admin = stores.login(email)
                if (admin != null && admin.pass == password) {
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