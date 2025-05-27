package com.example.project.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.SlideModel
import com.example.project.Model.Nav_Home
import com.example.project.Model.ProductBuy
import com.example.project.Model.ProductFull
import com.example.project.R
import com.example.project.Repository.Colors
import com.example.project.Repository.EvaluateRepo
import com.example.project.Repository.Imgs
import com.example.project.Repository.Products
import com.example.project.Repository.Sizes
import com.example.projectmanage.Model.Entity.Evaluate
import kotlinx.coroutines.launch

class HomeViewModel():ViewModel() {
    private val _nav = MutableLiveData<List<Nav_Home>>()
    val nav:LiveData<List<Nav_Home>> = _nav

    private val _banner = MutableLiveData<List<SlideModel>>()
    val banners : LiveData<List<SlideModel>> = _banner

    private val _listProduct = MutableLiveData<List<ProductFull>>()
    val listProduct:LiveData<List<ProductFull>> = _listProduct

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var products = Products()
    private var sizes = Sizes()
    private var colors = Colors()
    private var imgs = Imgs()
    private val evaluateRepo = EvaluateRepo()

    fun loadBanners(){
        val bannerList = listOf(
            SlideModel(R.drawable.cate1,"Laptop"),
            SlideModel(R.drawable.cate2,"Điện Thoại"),
            SlideModel(R.drawable.cate3,"Đồng Hồ"),
            SlideModel(R.drawable.quan,"Quần"),
            SlideModel(R.drawable.ao,"Áo")
        )
        _banner.value = bannerList
    }
    fun loadNav(){
        val navList = listOf(
            Nav_Home(R.drawable.ic_discount,"Mã Giảm Giá"),
            Nav_Home(R.drawable.ic_hot,"Sản Phẩm Bán Chạy"),
            Nav_Home(R.drawable.khuyen_mai,"Khuyến Mãi"),
            Nav_Home(R.drawable.ic_myorder,"Đơn Hàng Của Tôi"),
            Nav_Home(R.drawable.ic_hot,"Sản Phẩm Mới Ra Mắt")
        )
        _nav.value = navList
    }
    fun loadProduct(){
        viewModelScope.launch {
            try {
                val productList = products.selectAll()?:emptyList()
                val productFulls = mutableListOf<ProductFull>()
                for(pro in productList){
                    val id = pro.id
                    updateStar(id)
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

    fun selectEvaluate(proId: String){
        viewModelScope.launch {
            try {

            }catch (e: Exception){
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
                Log.d("avg","avg : $avg2Decimals")
                products.updateStar(proId, avg2Decimals)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}