package com.example.project.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.Customer
import com.example.project.Repository.Customers
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val customers = Customers()

    fun register(name: String, email: String, sdt: String, password: String) {
        if (name.isEmpty() || email.isEmpty() || sdt.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Vui lòng nhập đầy đủ thông tin"
            return
        }
        viewModelScope.launch {
            try {
                val checkEmail = customers.checkEmail(email)
                if (checkEmail){
                    _errorMessage.value = "Email đã được sử dụng"
                }else{
                    val k = customers.save("",name,email,sdt,"",password,"")
                    if (k){
                        _registerSuccess.value = true
                    }else{
                        _errorMessage.value = "Đăng ký thất bại"
                    }
                }
            }catch (e: Exception){
                _errorMessage.value = e.message
            }
        }
    }
}
