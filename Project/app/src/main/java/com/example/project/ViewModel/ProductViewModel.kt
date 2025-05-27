package com.example.project.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.Admin
import com.example.project.Model.ProductFull
import com.example.project.Repository.Carts
import com.example.project.Repository.EvaluateRepo
import com.example.project.Repository.Products
import com.example.project.Repository.StoreRepo
import com.example.projectmanage.Model.Entity.Evaluate
import kotlinx.coroutines.launch

class ProductViewModel():ViewModel() {
    private val _product = MutableLiveData<ProductFull>()
    val product: LiveData<ProductFull> = _product

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _pic = MutableLiveData<List<String>>()
    var pic:LiveData<List<String>> = _pic

    private val _size = MutableLiveData<List<String>>()
    var size:LiveData<List<String>> = _size

    private val _color = MutableLiveData<List<String>>()
    var color:LiveData<List<String>> = _color

    private val _storeInfo = MutableLiveData<Admin>()
    val storeInfo : LiveData<Admin> = _storeInfo

    private val _listEvaluate = MutableLiveData<List<Evaluate>>()
    val listEvaluate : LiveData<List<Evaluate>> = _listEvaluate

    private val carts = Carts()
    private val storeRepo = StoreRepo()
    private val evaluateRepo = EvaluateRepo()
    private var products = Products()

    fun init(pro: ProductFull){
        _product.value = pro

        _pic.value = pro.imgs

        _size.value = pro.sizes

        _color.value = pro.colors
    }

    fun addCart(cusId:String, proId: String, name: String, img: String, size: String, color: String, quantity: String, price: String){
        viewModelScope.launch {
            try {
                carts.save(cusId, proId, name, img, size, color, quantity, price)
                _success.value = true
            }catch (e: Exception){
                _error.value = "thêm thất bại"
            }
        }
    }

    fun getStore(id: String){
        viewModelScope.launch {
            try {
                _storeInfo.value = storeRepo.getStore(id)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
    fun selectEvaluate(proId: String) {
        viewModelScope.launch {
            try {
                _listEvaluate.value = evaluateRepo.selectOnePro(proId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}