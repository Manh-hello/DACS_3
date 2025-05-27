package com.example.project.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.Cart
import com.example.project.Model.Entity.Customer
import com.example.project.Model.Entity.MyDiscount
import com.example.project.Model.Entity.Product
import com.example.project.Repository.Carts
import com.example.project.Repository.Colors
import com.example.project.Repository.Customers
import com.example.project.Repository.MyDiscounts
import com.example.project.Repository.Orders
import com.example.project.Repository.Products
import com.example.project.Repository.Sizes
import kotlinx.coroutines.launch

class CartViewModel():ViewModel() {
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _listItem = MutableLiveData<List<Cart>>()
    val listItem:LiveData<List<Cart>> = _listItem

    private val _list1 = MutableLiveData<List<MyDiscount>>()
    val list1:LiveData<List<MyDiscount>> = _list1

    private val _listColor = MutableLiveData<List<String>>()
    val listColor:LiveData<List<String>> = _listColor

    private val _listSize = MutableLiveData<List<String>>()
    val listSize: LiveData<List<String>> = _listSize

    private val carts = Carts()
    private val colors = Colors()
    private val sizes = Sizes()
    private val products = Products()
    private val myDiscounts = MyDiscounts()
    private val orders = Orders()
    private val cus = Customers()

    private val _cusId = MutableLiveData<String>()
    val cusId: LiveData<String> = _cusId

    private val _cust = MutableLiveData<Customer?>()
    val cust : LiveData<Customer?> = _cust

    fun loadInfo(id: String){
        viewModelScope.launch {
            try {
                _cust.value = cus.select(id)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun loadList(cusId: String){
        viewModelScope.launch {
            try {
                _cusId.value = cusId
                val listItem = carts.select(cusId)
                Log.d("item","cus Id : $cusId")
                _listItem.value = listItem.reversed()
            }catch (e: Exception){
                _error.value = "Lỗi load danh sách"
            }
        }
    }
    fun loadDiscount(cusId: String){
        viewModelScope.launch {
            try {
                _list1.value = myDiscounts.select(cusId)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun addQuantity(cartId: String) {
        viewModelScope.launch {
            try {
                carts.addQuantity(cartId)
                loadList(cusId.value.toString())
            } catch (e: Exception) {
                _error.value = "Lỗi tăng số lượng: ${e.message}"
            }
        }
    }

    fun subQuantity(cartId: String) {
        viewModelScope.launch {
            try {
                carts.subQuantity(cartId)
                loadList(cusId.value.toString())
            } catch (e: Exception) {
                _error.value = "Lỗi giảm số lượng: ${e.message}"
            }
        }
    }

    fun delete(cartId: String) {
        viewModelScope.launch {
            try {
                carts.delete(cartId)
                loadList(cusId.value.toString())
            } catch (e: Exception) {
                _error.value = "Lỗi xóa sản phẩm: ${e.message}"
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

    fun saveMoreOne(cusId: String, reduce: String, pay: String, total: String, ds: MutableList<Cart>,time: String){
        viewModelScope.launch {
            try {
                val orderId = orders.save(cusId,reduce,pay,total)
                for (i in ds){
                    orders.saveDetail(orderId,i.proId,i.img,i.name,i.price,i.quantity,i.color,i.size,time)
                    products.update(i.proId,i.quantity)
                }
                _success.value = true
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun clear(cusId: String){
        viewModelScope.launch {
            try {
                carts.clear(cusId)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
}