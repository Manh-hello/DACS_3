package com.example.projectmanage.Activity

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.projectmanage.R
import com.example.projectmanage.ViewModel.DiscountViewModel
import com.example.projectmanage.databinding.ActivityAddDiscountBinding

class AddDisCount : AppCompatActivity() {
    private lateinit var binding: ActivityAddDiscountBinding
    private val myViewModel: DiscountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.btnAdd.setOnClickListener {
            save()
        }
        myViewModel.error.observe ( this, Observer { error ->
            Toast.makeText(this,error, Toast.LENGTH_SHORT).show()
        } )
        myViewModel.success.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        })

    }
    private fun save(){
        val storedId = getSharedPreferences("id_store", MODE_PRIVATE)
            .getString("id_store", null).orEmpty()
        val code = binding.edtCode.text.toString()
        val reduce = binding.edtReduce.text.toString()
        val quantity = binding.edtQuantity.text.toString()
        if(code.isEmpty()||reduce.isEmpty()||quantity.isEmpty()){
            Toast.makeText(this,"Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show()
        }else{
            myViewModel.saveDiscount(storedId,code,reduce,quantity.toInt())
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
}