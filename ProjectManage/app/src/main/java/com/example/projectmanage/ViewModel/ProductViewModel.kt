package com.example.projectmanage.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanage.Model.ProductFull
import com.example.projectmanage.Repository.Colors
import com.example.projectmanage.Repository.Imgs
import com.example.projectmanage.Repository.Products
import com.example.projectmanage.Repository.Sizes
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.s

class ProductViewModel : ViewModel() {

    private val _listProduct = MutableLiveData<List<ProductFull>>()
    val listProduct: LiveData<List<ProductFull>> = _listProduct

    private val _product = MutableLiveData<ProductFull?>()
    val product : LiveData<ProductFull?> = _product

    private val _pic = MutableLiveData<List<String>>()
    var pic:LiveData<List<String>> = _pic

    private val _size = MutableLiveData<List<String>>()
    var size:LiveData<List<String>> = _size

    private val _color = MutableLiveData<List<String>>()
    var color:LiveData<List<String>> = _color

    private var products = Products()
    private var imgs = Imgs()
    private var sizes = Sizes()
    private var colors = Colors()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isSuccessU = MutableLiveData<Boolean>()
    val isSuccessU: LiveData<Boolean> = _isSuccessU

    private val _isSuccessD = MutableLiveData<Boolean>()
    val isSuccessD: LiveData<Boolean> = _isSuccessD

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun init(pro: ProductFull?){
        _product.value = pro

        _pic.value = pro?.imgs

        _size.value = pro?.sizes

        _color.value = pro?.colors
    }

    fun addProduct(
        storeId: String,
        name: String,
        price: String,
        quantity: String,
        desc: String,
        color: String,
        size: String,
        imageUris: List<Uri>
    ) {
        viewModelScope.launch {
            try {
                var id = products.save(storeId,name,price,desc,quantity.toInt())

                val colorList = color.split(",").map { it.trim() }
                colors.save(colorList, id)

                val sizeList = size.split(",").map { it.trim() }
                sizes.save(sizeList, id)

                imgs.uploadImages(id, imageUris)

                _isSuccess.value = true

            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun selectProduct(storeId: String) {
        viewModelScope.launch {
            try {
                val productList = products.select(storeId)?:emptyList()
                var fullProducts = mutableListOf<ProductFull>()

                for(pro in productList){
                    val proId = pro.id ?:continue
                    val s = sizes.select(proId)
                    val c = colors.select(proId)
                    val i = imgs.select(proId)
                    val proFull = ProductFull(
                        product = pro,
                        sizes = s.toMutableList(),
                        colors = c.toMutableList(),
                        imgs = i.toMutableList()
                    )
                    fullProducts.add(proFull)

                }
                _listProduct.postValue(fullProducts)
            }catch (e: Exception){
                Log.e("ProductViewModel", "Error when selecting product", e)
                _error.value = e.message
                _listProduct.postValue(emptyList())
            }
        }
    }

    fun update(proId: String, name: String, price: String, quantity: String, desc: String, color: String, size: String){
        viewModelScope.launch {
            try {
                products.update(proId,name,price,desc,quantity.toInt())

                val colorList = color.split(",").map { it.trim() }
                colors.update(proId, colorList)

                val sizeList = size.split(",").map { it.trim() }
                sizes.update(proId, sizeList)
                _isSuccessU.value = true
            }catch (e: Exception){
                _error.value = "cập nhật thất bại!"
            }
        }
    }

    fun delete(proId: String){
        viewModelScope.launch {
            try {
                products.delete(proId)
                colors.delete(proId)
                sizes.delete(proId)
                imgs.delete(proId)
                _isSuccessD.value = true
            }catch (e: Exception){
                _error.value = "xóa thất bại"
            }
        }
    }
}
