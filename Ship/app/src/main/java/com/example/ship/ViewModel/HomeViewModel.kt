package com.example.ship.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ship.Model.Customer
import com.example.ship.Model.OrderDetail
import com.example.ship.Repository.OrderRepo
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _listOrder = MutableLiveData<List<OrderDetail>>()
    val listOrder : LiveData<List<OrderDetail>> = _listOrder

    private val _cusName = MutableLiveData<Customer>()
    val cusName : LiveData<Customer> = _cusName

    private val _count1 = MutableLiveData<Int>()
    val count1 : LiveData<Int> = _count1

    private val _count2 = MutableLiveData<Int>()
    val count2 : LiveData<Int> = _count2

    private val _count3 = MutableLiveData<Int>()
    val count3 : LiveData<Int> = _count3

    private val orderRepo = OrderRepo()

    fun selectAllOrder(){
        viewModelScope.launch {
            try {
                _listOrder.value = orderRepo.selectAllOrderDetail().reversed()
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun get(id: String, shiperId: String){
        viewModelScope.launch {
            try {
                orderRepo.update(id,shiperId)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun done(id: String){
        viewModelScope.launch {
            try {
                orderRepo.updateSuccess(id)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun getName(orderId: String){
        viewModelScope.launch {
            try {
                _cusName.value = orderRepo.loadNameCus(orderId)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun getCount(spId: String){
        viewModelScope.launch {
            try {
                val listAll = orderRepo.selectAllOrderDetail().filter { it.shiper == spId }
                var countDangGiao = 0
                var countDaGiao = 0
                var total = 0

                for (order in listAll) {
                    when (order.status) {
                        "Đang giao" -> {
                            countDangGiao++
                        }
                        "Đã nhận" -> {
                            countDaGiao++
                            total += 30
                        }
                    }
                }
                _count1.value = countDangGiao
                _count2.value = countDaGiao
                _count3.value = total
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

}