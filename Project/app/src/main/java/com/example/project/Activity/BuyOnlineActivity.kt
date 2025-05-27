package com.example.project.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.project.Model.Entity.Cart
import com.example.project.R
import com.example.project.ViewModel.BellViewModel
import com.example.project.ViewModel.CartViewModel
import com.example.project.ViewModel.ConfirmBuyViewModel
import com.example.project.databinding.ActivityBuyOnlineBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.getValue

class BuyOnlineActivity : AppCompatActivity() {
    private val myViewModel: ConfirmBuyViewModel by viewModels()
    private val myViewModel1: CartViewModel by viewModels()
    private val bellViewModel : BellViewModel by viewModels()
    private lateinit var binding: ActivityBuyOnlineBinding
    private lateinit var cusId: String
    private lateinit var pay: String
    private lateinit var proName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyOnlineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.ping)

        cusId = intent.getStringExtra("cusId").toString()
        pay = intent.getStringExtra("pay").toString()
        proName = intent.getStringExtra("proName").toString()

        binding.apply {
            tvCusId.setText(cusId)
            tvPay.setText(pay)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnPay.setOnClickListener {
            binding.apply {
                dimView.visibility = View.VISIBLE
                probar.visibility = View.VISIBLE
                btnPay.isClickable = false
                btnBack.isClickable = false
            }
            if(intent.getStringExtra("type").toString() == "1"){
                handle1()
            }else if(intent.getStringExtra("type").toString() == "2"){
                handle2()
            }

        }

    }
    fun handle1(){
        val id = intent.getStringExtra("proId").orEmpty()
        val url = intent.getStringExtra("url").orEmpty()
        val name = intent.getStringExtra("name").orEmpty()
        val color = intent.getStringExtra("color").orEmpty()
        val size = intent.getStringExtra("size").orEmpty()
        val price = intent.getStringExtra("price").orEmpty()
        val quantity = intent.getIntExtra("quantity",1)
        val reduce = intent.getIntExtra("reduce",0)
        val total = intent.getIntExtra("total",0)
        val disId = intent.getStringExtra("disId").toString()
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formartDate = format.format(calendar.time)

        myViewModel.save(cusId,"${reduce}k","0k","${total}k", id,url,name,price,quantity,color,size,formartDate)
        bellViewModel.save(cusId,"Đặt Hàng","Đặt hàng thanh công","Bạn đã đặt hàng thành công, đơn hàng sẽ sớm giao đến bạn",formartDate)
        if (reduce!=0){
            myViewModel.updateMyDiscount(disId)
        }
        myViewModel.updatePro(id,quantity)
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this, BuySuccessActivity::class.java)
            i.putExtra("proName", proName)
            startActivity(i)
            finish()
        }, 2000)
    }
    fun handle2(){
        val total = intent.getIntExtra("total", 0)
        val reduce = intent.getIntExtra("reduce", 0)
        val disId = intent.getStringExtra("disId").orEmpty()
        val list = intent.getSerializableExtra("list") as? ArrayList<Cart> ?: arrayListOf()
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formartDate = format.format(calendar.time)

        if (reduce != 0) {
            myViewModel.updateMyDiscount(disId)
        }
        myViewModel1.clear(cusId)

        myViewModel1.saveMoreOne(
            cusId,
            "${reduce}k",
            "0k",
            "${total}k",
            list,
            formartDate
        )
        bellViewModel.save(
            cusId,
            "Đặt Hàng",
            "Đặt hàng thanh công",
            "Bạn đã đặt hàng thành công, đơn hàng sẽ sớm giao đến bạn",
            formartDate
        )

        myViewModel1.success.observe(this, Observer {
            startActivity(Intent(this, BuySuccessActivity::class.java))
        })
    }
}