package com.example.project.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.ProductFull
import com.example.project.Repository.Colors
import com.example.project.Repository.Imgs
import com.example.project.Repository.Products
import com.example.project.Repository.Sizes
import kotlinx.coroutines.launch

class BuySuccessViewModel: ViewModel() {
    private var products = Products()
    private var sizes = Sizes()
    private var colors = Colors()
    private var imgs = Imgs()

    private val _listProduct = MutableLiveData<List<ProductFull>>()
    val listProduct:LiveData<List<ProductFull>> = _listProduct

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadProduct(proName: String){
        viewModelScope.launch {
            try {
                val productList = products.searchByName(proName)?:emptyList()
                val productFulls = mutableListOf<ProductFull>()
                for(pro in productList){
                    val id = pro.id
                    var s = sizes.select(id)
                    var c = colors.select(id)
                    var i = imgs.select(id)
                    val proFull = ProductFull(
                        pro,
                        s.toMutableList(),
                        c.toMutableList(),
                        i.toMutableList()
                    )
                    productFulls.add(proFull)
                    _listProduct.value = productFulls
                }
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
}