package com.example.projectmanage.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanage.Model.Entity.Customer
import com.example.projectmanage.Model.Entity.OrderDetail
import com.example.projectmanage.Repository.Orders
import com.example.projectmanage.Repository.Products
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _orderDetails = MutableLiveData<List<OrderDetail>>()
    val orderDetails: LiveData<List<OrderDetail>> = _orderDetails

    private val _count1 = MutableLiveData<Int>()
    val count1 : LiveData<Int> = _count1

    private val _count2 = MutableLiveData<Int>()
    val count2 : LiveData<Int> = _count2

    private val _cusName = MutableLiveData<Customer>()
    val cusName : LiveData<Customer> = _cusName

    private val _count3 = MutableLiveData<Int>()
    val count3 : LiveData<Int> = _count3

    private val ordersRepo = Orders()
    private val productsRepo = Products()

    fun loadOrderDetail(storeId: String){
        viewModelScope.launch {
            try {
                val listAllOrderDt = ordersRepo.selectAll()
                val myPro = productsRepo.select(storeId)?:emptyList()
                val myProductIds = myPro.map { it.id }.toSet()

                val filtered = listAllOrderDt.filter { it.productId in myProductIds }

                _orderDetails.value = filtered.reversed()
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun updateSuccess(id: String){
        viewModelScope.launch {
            try {
                ordersRepo.updateAccess(id)
            }catch (e: Exception){
                _error.value = e.message
                Log.d("error", "error : ${_error.value}")
            }
        }
    }

    fun updateError(id: String){
        viewModelScope.launch {
            try {
                ordersRepo.updateError(id)
            }catch (e: Exception){
                _error.value = e.message
                Log.d("error", "error : ${_error.value}")
            }
        }
    }

    fun getCount(storeId: String){
        viewModelScope.launch {
            try {
                val listAllOrderDt = ordersRepo.selectAll()
                val myPro = productsRepo.select(storeId)?:emptyList()
                val myProductIds = myPro.map { it.id }.toSet()
                val filtered = listAllOrderDt.filter { it.productId in myProductIds }

                var daBan = 0
                var dangGiao = 0
                var doanhThu = 0
                for (i in filtered){
                    when(i.status){
                        "Đã nhận"->{
                            daBan++
                            val price = i.price.replace("k","", ignoreCase = true).trim().toInt()
                            doanhThu += price * i.quantity
                        }
                        "Đang giao", "Đã giao"->{
                            dangGiao++
                        }
                    }
                }
                _count1.value = daBan
                _count2.value = dangGiao
                _count3.value = doanhThu
            }catch (e: Exception){_error.value = e.message}
        }
    }
    fun getName(orderId: String){
        viewModelScope.launch {
            try {
                _cusName.value = ordersRepo.loadNameCus(orderId)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
}