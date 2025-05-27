package com.example.project.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.Cart
import com.example.project.Model.Entity.MyDiscount
import com.example.project.Model.Item_ConfirmBuy
import com.example.project.Model.ProductBuy
import com.example.project.R
import com.example.project.Repository.MyDiscounts
import com.example.project.Repository.Orders
import com.example.project.Repository.Products
import kotlinx.coroutines.launch

class ConfirmBuyViewModel():ViewModel() {
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _list = MutableLiveData<List<Item_ConfirmBuy>>()
    var list:LiveData<List<Item_ConfirmBuy>> = _list

    private val _discountList = MutableLiveData<List<MyDiscount>>()
    val discountList: LiveData<List<MyDiscount>> = _discountList

    private val myDiscounts = MyDiscounts()
    private val orders = Orders()
    private val products = Products()

    fun loadDiscount(cusId: String){
        viewModelScope.launch {
            try {
                _discountList.value = myDiscounts.select(cusId)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun save(cusId: String, reduce:String, pay:String, total:String, productId:String, src: String, name: String, price: String, quantity:Int, color:String, size: String, time: String){
        viewModelScope.launch {
            try {
                val orderId = orders.save(cusId,reduce,pay,total)
                orders.saveDetail(orderId,productId,src,name,price,quantity,color,size,time)
                _success.value = true
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun saveMoreOne(cusId: String, reduce: String, pay: String, total: String, ds: MutableList<Cart>,time: String){
        viewModelScope.launch {
            try {
                val orderId = orders.save(cusId,reduce,pay,total)
                for (i in ds){
                    orders.saveDetail(orderId,i.proId,i.img,i.name,i.price,i.quantity,i.color,i.size,time)
                }
                _success.value = true
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun updateMyDiscount(id: String){
        viewModelScope.launch {
            try {
                myDiscounts.update(id)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun updatePro(id: String, quantity:Int){
        viewModelScope.launch {
            try {
                products.update(id,quantity)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

}