package com.example.project.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.MyDiscount
import com.example.project.Repository.Discounts
import com.example.project.Repository.MyDiscounts
import com.example.projectmanage.Model.Entity.Discount
import kotlinx.coroutines.launch

class DiscountViewModel: ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success : LiveData<Boolean> = _success

    private val _list = MutableLiveData<List<Discount>>()
    val list : LiveData<List<Discount>> = _list

    private val _list2 = MutableLiveData<List<MyDiscount>>()
    val list2 : LiveData<List<MyDiscount>> = _list2

    private val myDiscounts = MyDiscounts()
    private val discounts = Discounts()

    fun selectAll(){
        viewModelScope.launch {
            try {
                _list.value = discounts.selectAll().filter { it.count>0 }
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun select(cusId: String){
        viewModelScope.launch {
            try {
                _list2.value = myDiscounts.select(cusId)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun save(cusId:String, discountId:String, code:String, reduce: String, onFinished: (() -> Unit)? = null){
        viewModelScope.launch {
            try {
                discounts.update(discountId)
                myDiscounts.save(cusId,discountId,code,reduce)
                _success.value = true
                onFinished?.invoke()
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun update(id: String){
        viewModelScope.launch {
            try {
                myDiscounts.update(id)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
}