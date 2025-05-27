package com.example.project.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.Customer
import com.example.project.Repository.Customers
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _user = MutableLiveData<Customer?>()
    val user: LiveData<Customer?> get() = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val customers = Customers()

    fun loadUser(userId: String) {
        viewModelScope.launch {
            try {
                val customer = customers.select(userId)
                _user.value = customer
            }catch (e: Exception){
                _error.value = e.message
            }

        }
    }
    fun updateUser(id: String,
                   name:String,
                   sdt:String,
                   address:String,
                   password:String,
                   birth:String) {
        viewModelScope.launch {
            try {
                customers.update(id,name,sdt,address,password,birth)
                _success.value = true
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
    fun updateUser(id: String,
                   name:String,
                   sdt:String,
                   address:String,
                   password:String,
                   birth:String,
                   src: Uri){
            viewModelScope.launch {
                try {
                    customers.update(id, src,name,sdt,address,password,birth)
                    _success.value = true
                }catch (e: Exception){
                    _error.value = e.message
                }
            }
    }
    fun logOut(){
        _user.value = null
    }
}
