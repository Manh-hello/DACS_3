package com.example.ship.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ship.Repository.ShiperRepo
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel() {
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success : LiveData<Boolean> = _success

    private val _login = MutableLiveData<String>()
    val login : LiveData<String> = _login

    private val shiperRepo = ShiperRepo()

    fun register(name: String, email: String, sdt: String, password: String, address: String) {
        viewModelScope.launch {
            try {
                val checkEmail = shiperRepo.checkEmail(email)
                if (checkEmail){
                    _error.value = "Email đã được sử dụng"
                }else{
                    val k = shiperRepo.save(name,email,sdt,password,address)
                    if (k){
                        _success.value = true
                    }else{
                        _error.value = "Đăng ký thất bại"
                    }
                }
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun login(email: String, pass: String){
        viewModelScope.launch {
            try {
                val shiper = shiperRepo.login(email)
                if (shiper!=null && shiper.pass == pass){
                    _login.value = shiper.id
                }else{
                    _error.value = "sai email hoặc mật khẩu"
                }
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
}