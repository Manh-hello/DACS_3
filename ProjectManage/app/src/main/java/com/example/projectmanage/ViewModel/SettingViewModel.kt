package com.example.projectmanage.ViewModel

import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanage.Model.Entity.Admin
import com.example.projectmanage.Repository.Stores
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.net.URI

class SettingViewModel:ViewModel() {
    private val _info = MutableLiveData<Admin?>()
    val info: LiveData<Admin?> = _info

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private var store = Stores()

    fun loadData(id: String) {
        if (id.isBlank()) {
            _error.value = "ID trống, không thể load dữ liệu"
            return
        }
        viewModelScope.launch {
            try {
                val admin = store.select(id)
                _info.value = admin
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun update(storeId: String, pass: String, address: String, phone: String, name: String) {
        viewModelScope.launch {
            try {
                store.update(storeId, pass, address, phone, name)
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun update(storeId: String, pass: String, address: String, phone: String, name: String, imageUri: Uri) {
        viewModelScope.launch {
            try {
                store.update(storeId, pass, address, phone, name, imageUri)
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun logOut(){
        _info.value = null
    }
}