package com.example.projectmanage.Activity

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanage.Adapter.ListImgProduct
import com.example.projectmanage.Adapter.ListImgProduct1
import com.example.projectmanage.Model.Entity.Product
import com.example.projectmanage.Model.ProductFull
import com.example.projectmanage.ViewModel.ProductViewModel
import com.example.projectmanage.databinding.ActivityFixProductBinding
import kotlin.toString

class FixProduct : AppCompatActivity() {
    private lateinit var binding: ActivityFixProductBinding
    private val myViewModel: ProductViewModel by viewModels()
    private lateinit var proId: String
    private lateinit var product: Product
    private var listImg = mutableListOf<String>()
    private var listSize = mutableListOf<String>()
    private var listColor = mutableListOf<String>()
    private lateinit var adapter: ListImgProduct1
    private lateinit var storeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFixProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val proFull = intent.getParcelableExtra<ProductFull>("proFull")
        proId = proFull?.product?.id ?: run {
            Toast.makeText(this, "Không có id sản phẩm", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        myViewModel.init(proFull)
        bindProductInfo(proFull)

        binding.btnBack.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.btnUpdate.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val price = binding.edtPrice.text.toString().trim()
            val quantity = binding.edtQuantity.text.toString().trim()
            val desc = binding.edtDesc.text.toString().trim()
            val color = binding.edtColor.text.toString().trim()
            val size = binding.edtSize.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() ||
                desc.isEmpty()
            ) {
                Toast.makeText(this, "Bạn chưa điền đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                myViewModel.update(proId, name, price, quantity, desc, color, size)
            }
            myViewModel.isSuccessU.observe(this, Observer { success ->
                if (success) {
                    Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            })
        }
        binding.btnDelete.setOnClickListener {
            myViewModel.delete(proId)
            myViewModel
        }
        myViewModel.isSuccessU.observe(this) { success ->
            if (success) {
                myViewModel.selectProduct(storeId)
                setResult(RESULT_OK)
                finish()
            }
        }
        myViewModel.isSuccessD.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show()
                myViewModel.selectProduct(storeId)
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        currentFocus?.let {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun bindProductInfo(product: ProductFull?) {
        binding.apply {
            edtName.setText(product?.product?.name)
            edtQuantity.setText(product?.product?.quantity.toString())
            edtPrice.setText(product?.product?.price)
            edtDesc.setText(product?.product?.desc)
            proId = product?.product?.id ?: ""
            storeId = getSharedPreferences("id_store", MODE_PRIVATE)
                .getString("id_store", null).orEmpty()
            val colorString = product?.colors?.joinToString(", ") ?: ""
            val sizeString = product?.sizes?.joinToString(", ") ?: ""

            edtColor.setText(colorString)
            edtSize.setText(sizeString)

            adapter = ListImgProduct1(product?.imgs)
            rvListImg.adapter = adapter
            rvListImg.layoutManager =
                LinearLayoutManager(this@FixProduct, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}