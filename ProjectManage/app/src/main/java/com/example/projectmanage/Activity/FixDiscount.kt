package com.example.projectmanage.Activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.projectmanage.Model.Entity.Discount
import com.example.projectmanage.ViewModel.DiscountViewModel
import com.example.projectmanage.databinding.ActivityFixDiscountBinding

class FixDiscount : AppCompatActivity() {
    private lateinit var binding: ActivityFixDiscountBinding
    private lateinit var disId : String
    private val myViewModel : DiscountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFixDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dis = intent.getParcelableExtra<Discount>("discount")
        disId = dis?.id.toString()
        myViewModel.init(dis)
        info(dis)

        binding.btnBack.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.btnUpdate.setOnClickListener {
            val reduce = binding.edtReduce.text.toString()
            val quantity = binding.edtQuantity.text.toString()

            if (reduce.isEmpty() || quantity.isEmpty()){
                Toast.makeText(this,"vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }else{
                myViewModel.update(disId, quantity,reduce)
                myViewModel.successU.observe(this, Observer {
                    Toast.makeText(this, "cập nhật thành công", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                })
            }
        }
        binding.btnDelete.setOnClickListener {
            myViewModel.delete(disId)
            myViewModel.successD.observe(this, Observer {
                Toast.makeText(this, "xóa thành công", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            })
        }
    }
    private fun info(dis: Discount?){
        myViewModel.discount.observe(this, Observer{
            binding.apply {
                edtCode.setText(dis?.code)
                edtQuantity.setText(dis?.count?.toInt().toString())
                edtReduce.setText(dis?.reduce)
            }
        })
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