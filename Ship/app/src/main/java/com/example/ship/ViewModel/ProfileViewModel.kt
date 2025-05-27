package com.example.ship.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ship.Model.Shiper
import com.example.ship.Repository.ShiperRepo
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _spInfo = MutableLiveData<Shiper>()
    val spInfo: LiveData<Shiper> = _spInfo

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success : LiveData<Boolean> = _success

    private val spRepo = ShiperRepo()

    fun loadInfo(id: String){
        viewModelScope.launch {
            try {
                _spInfo.value = spRepo.load(id)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun update(shiperId: String, name: String, sdt: String, address: String, password: String, birth: String){
        viewModelScope.launch {
            try {
                spRepo.update(shiperId,name,password,sdt,address,birth)
                _success.value = true
            }catch (e: Exception){
                _error.value  = e.message
            }
        }
    }
    fun update(shiperId: String, name: String, sdt: String, address: String, password: String, birth: String, selectedImageUri: Uri){
        viewModelScope.launch {
            try {
                spRepo.update(shiperId,selectedImageUri,name,sdt,address,password,birth)
                _success.value = true
            }catch (e: Exception){
                _error.value  = e.message
            }
        }
    }
}