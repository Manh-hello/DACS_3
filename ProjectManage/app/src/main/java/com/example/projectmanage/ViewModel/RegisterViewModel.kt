package com.example.projectmanage.ViewModel

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
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterViewModel:ViewModel() {
    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val stores = Stores()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun register(name: String, email: String, phone: String, password: String) {
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Vui lòng nhập đầy đủ thông tin"
            return
        }
        viewModelScope.launch {
            try {
                val checkEmail = stores.checkEmail(email)
                if(checkEmail){
                    _errorMessage.value = "Email đã được sử dụng"
                    return@launch
                }
                val calendar = Calendar.getInstance()
                val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val formartDate = format.format(calendar.time)
                val k = stores.save(name,email,password,"",phone,"",formartDate)
                if(k){
                    _registerSuccess.value = true
                }else{
                    _errorMessage.value = "Đăng ký thất bại"
                }
            }catch (e: Exception){
                _errorMessage.value = e.message ?: "Đã xảy ra lỗi"
            }
        }
    }
}