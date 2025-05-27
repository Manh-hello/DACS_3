package com.example.project.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Repository.EvaluateRepo
import com.example.project.Repository.Orders
import com.example.project.Repository.Products
import com.example.project.Repository.ShiperRepo
import com.example.projectmanage.Model.Entity.Order
import com.example.projectmanage.Model.Entity.OrderDetail
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val _listOrder = MutableLiveData<List<Order>>()
    val listOrder: LiveData<List<Order>> = _listOrder

    private val _mapOrderDetails = MutableLiveData<Map<String, List<OrderDetail>>>()
    val mapOrderDetails: LiveData<Map<String, List<OrderDetail>>> = _mapOrderDetails

    private val _orDt = MutableLiveData<OrderDetail>()
    val orDt: LiveData<OrderDetail> = _orDt

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val orders = Orders()
    private val shiperRepo = ShiperRepo()
    private val evaluateRepo = EvaluateRepo()
    private val productRepo = Products()

    private val _spName = MutableLiveData<String>()
    val spName: LiveData<String> = _spName

    fun loadOrder(cusId: String) {
        viewModelScope.launch {
            try {
                _listOrder.value = orders.selectOrder(cusId).reversed()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun resetSuccess() {
        _success.value = false
    }

    fun loadOrderDetailWait(orderIds: List<String>) {
        viewModelScope.launch {
            try {
                val map = mutableMapOf<String, List<OrderDetail>>()
                for (id in orderIds) {
                    val details = orders.selectOrderDetail(id)
                        .filter { it.status == "Chờ cửa hàng xác nhận" }
                    if (details.isNotEmpty()) {
                        map[id] = details
                    }
                }
                _mapOrderDetails.value = map
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadOrderDetailCancel(orderIds: List<String>) {
        viewModelScope.launch {
            try {
                val map = mutableMapOf<String, List<OrderDetail>>()
                for (id in orderIds) {
                    val details = orders.selectOrderDetail(id)
                        .filter { it.status == "Đã hủy" }
                    if (details.isNotEmpty()) {
                        map[id] = details
                    }
                }
                _mapOrderDetails.value = map
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadOrderDetailAlready(orderIds: List<String>) {
        viewModelScope.launch {
            try {
                val map = mutableMapOf<String, List<OrderDetail>>()
                for (id in orderIds) {
                    val details = orders.selectOrderDetail(id)
                        .filter { it.status == "Đã xác nhận" }
                    if (details.isNotEmpty()) {
                        map[id] = details
                    }
                }
                _mapOrderDetails.value = map
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadOrderDetailOnDelivery(orderIds: List<String>) {
        viewModelScope.launch {
            try {
                val map = mutableMapOf<String, List<OrderDetail>>()
                for (id in orderIds) {
                    val details = orders.selectOrderDetail(id)
                        .filter { it.status == "Đang giao" || it.status == "Đã giao" }
                    if (details.isNotEmpty()) {
                        map[id] = details
                    }
                }
                _mapOrderDetails.value = map
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadOrderDetailDelivered(orderIds: List<String>) {
        viewModelScope.launch {
            try {
                val map = mutableMapOf<String, List<OrderDetail>>()
                for (id in orderIds) {
                    val details = orders.selectOrderDetail(id)
                        .filter { it.status == "Đã nhận" }
                    if (details.isNotEmpty()) {
                        map[id] = details
                    }
                }
                _mapOrderDetails.value = map
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateSuccess(id: String) {
        viewModelScope.launch {
            try {
                orders.updateAccess(id)
            } catch (e: Exception) {
                _error.value = e.message
                Log.d("error", "error : ${_error.value}")
            }
        }
    }

    fun updateError(id: String) {
        viewModelScope.launch {
            try {
                orders.updateError(id)
            } catch (e: Exception) {
                _error.value = e.message
                Log.d("error", "error : ${_error.value}")
            }
        }
    }

    fun getName(id: String) {
        viewModelScope.launch {
            try {
                _spName.value = shiperRepo.getName(id)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun select(id: String) {
        viewModelScope.launch {
            try {
                _orDt.value = orders.selectOne(id)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun evaluate(orderId: String, proId: String, cusId: String, star: Int, message: String) {
        viewModelScope.launch {
            try {
                evaluateRepo.save(proId, cusId, message, star)
                orders.updateEvaluate(orderId)
                updateStar(proId)
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun evaluate(
        orderId: String,
        proId: String,
        cusId: String,
        star: Int,
        message: String,
        img: Uri
    ) {
        viewModelScope.launch {
            try {
                evaluateRepo.save(proId, cusId, message, star, img)
                orders.updateEvaluate(orderId)
                updateStar(proId)
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateStar(proId: String) {
        viewModelScope.launch {
            try {
                val list = evaluateRepo.selectOnePro(proId)
                val avg = list.map { it.rating }.average()
                val avg2Decimals = String.format("%.1f", avg).toDouble()
                productRepo.updateStar(proId, avg2Decimals)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}