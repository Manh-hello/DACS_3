package com.example.projectmanage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanage.Model.Entity.Discount
import com.example.projectmanage.Repository.Discounts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class DiscountViewModel:ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private  var discounts = Discounts()

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _successU = MutableLiveData<Boolean>()
    val successU: LiveData<Boolean> = _successU

    private val _successD = MutableLiveData<Boolean>()
    val successD: LiveData<Boolean> = _successD

    private val _listDiscount = MutableLiveData<List<Discount>>()
    val listDiscount: LiveData<List<Discount>> = _listDiscount

    private val _discount = MutableLiveData<Discount>()
    val discount : LiveData<Discount> = _discount

    fun init(dis: Discount?){
        _discount.value = dis
    }

    fun saveDiscount(storeId:String,
                             code:String,
                             reduce:String,
                             count:Int){
        viewModelScope.launch {
            try {
                discounts.save(storeId,code,reduce,count)
                _success.value = true
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
    fun selectAll(storeId:String){
        viewModelScope.launch {
            try {
                val list = discounts.select(storeId)
                _listDiscount.postValue(list)
            }catch (e: Exception){
                _error.value = e.message
                _listDiscount.postValue(emptyList())
            }
        }
    }
    fun update(id: String, quantity: String, reduce: String){
        viewModelScope.launch {
            try {
                discounts.update(id,quantity.toInt(),reduce)
                _successU.value = true
            }catch (e: Exception){
                _error.value = "Cập nhật thất bại!"
            }
        }
    }
    fun delete(id: String){
        viewModelScope.launch {
            try {
                discounts.delete(id)
                _successD.value = true
            }catch (e: Exception){
                _error.value = "Xóa thất bại"
            }
        }
    }
}