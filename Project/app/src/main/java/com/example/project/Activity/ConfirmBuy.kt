package com.example.project.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.Adapter.ItemConfirmBuyAdapter
import com.example.project.Model.Entity.MyDiscount
import com.example.project.Model.ProductBuy
import com.example.project.R
import com.example.project.ViewModel.BellViewModel
import com.example.project.ViewModel.ConfirmBuyViewModel
import com.example.project.databinding.ActivityConfirmBuyBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ConfirmBuy : AppCompatActivity() {
    private val myViewModel: ConfirmBuyViewModel by viewModels()
    private val bellViewModel : BellViewModel by viewModels()
    private lateinit var binding: ActivityConfirmBuyBinding
    private lateinit var adapter: ItemConfirmBuyAdapter
    private val list = mutableListOf<ProductBuy>()
    var list1: MutableList<MyDiscount> = mutableListOf<MyDiscount>()
    private lateinit var cusId: String
    private var total = 0
    private var reduce = 0
    private lateinit var disId: String
    private var pay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCusId()
        loadViewModel()
        setUI()
        setUpClick()
        setupRecyclerView()
        getDataFromIntent()
        setupListeners()
    }

    private fun loadCusId(){
        cusId = getSharedPreferences("id_customers", MODE_PRIVATE)
            .getString("id_customer", null).orEmpty()
    }

    private fun loadViewModel(){
        myViewModel.loadDiscount(cusId)
    }

    private fun setUpClick(){
        val id = intent.getStringExtra("proId").orEmpty()
        val url = intent.getStringExtra("url").orEmpty()
        val name = intent.getStringExtra("name").orEmpty()
        val color = intent.getStringExtra("color").orEmpty()
        val size = intent.getStringExtra("size").orEmpty()
        val price = intent.getStringExtra("price").orEmpty()
        val quantity = intent.getIntExtra("quantity",1)
        binding.btnBuy.setOnClickListener {
            val calendar = Calendar.getInstance()
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formartDate = format.format(calendar.time)
            if (binding.spnPay.selectedItem.toString() == "Tiền mặt"){
                myViewModel.save(cusId,"${reduce}k","${pay}k","${total}k", id,url,name,price,quantity,color,size,formartDate)
                bellViewModel.save(cusId,"Đặt Hàng","Đặt hàng thanh công","Bạn đã đặt hàng thành công, đơn hàng sẽ sớm giao đến bạn",formartDate)
                if (reduce!=0){
                    myViewModel.updateMyDiscount(disId)
                }
                myViewModel.updatePro(id,quantity)
            }else if(binding.spnPay.selectedItem.toString() == "Thanh toán qua Momo"){
                val inten = Intent(this, BuyOnlineActivity::class.java)
                inten.putExtra("type","1")
                inten.putExtra("cusId",cusId)
                inten.putExtra("pay",binding.tvPay.text)
                inten.putExtra("proName", name)
                inten.putExtra("reduce", reduce)
                inten.putExtra("total", total)
                inten.putExtra("disId", disId)
                inten.putExtra("proId", id)
                inten.putExtra("url", url)
                inten.putExtra("name", name)
                inten.putExtra("price", price)
                inten.putExtra("quantity", quantity)
                inten.putExtra("color", color)
                inten.putExtra("size", size)
                startActivity(inten)
            }
        }
        myViewModel.success.observe(this, Observer{i->
            if(i){
                val inten = Intent(this, BuySuccessActivity::class.java)
                inten.putExtra("proName", name)
                startActivity(inten)
                finish()
            }
        })
    }

    private fun setUI(){
        myViewModel.discountList.observe(this) { items ->
            list1.clear()
            val emptyDiscount = MyDiscount("", "", "", "","0k",true)
            list1.add(emptyDiscount)
            list1.addAll(items.filter { it.status == true })
            val discountCodes = list1.filter { it.status == true }.map { it.reduce }
            if(discountCodes.size !=0){
                val adapterDiscount = ArrayAdapter(this, R.layout.row_select_discount, discountCodes)
                binding.spinnerGender.adapter = adapterDiscount
                binding.spinnerGender.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedDiscount = list1[position]
                            reduce = selectedDiscount.reduce.replace("k", "", ignoreCase = true).trim().toInt()
                            binding.tvDiscount.text = selectedDiscount.reduce
                            disId = selectedDiscount.id
                            pay = total - reduce
                            if (pay<0){
                                pay=0
                            }
                            binding.tvPay.text = "${pay}k"
                            binding.tvTotal.text = "${total}k"
                            binding.tvDiscount.text = "${reduce}k"
                            binding.layout.visibility = View.VISIBLE
                            binding.progress.visibility = View.INVISIBLE
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            }else{
                pay = total
                binding.apply {
                    tvPay.text = "${pay}k"
                    tvTotal.text = "${total}k"
                    tvDiscount.text = "0k"
                }
                binding.layout.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            }

        }
        val listPay = listOf("Tiền mặt", "Thanh toán qua Momo")
        val adapterPay = ArrayAdapter(this,R.layout.row_select_discount,listPay)
        binding.spnPay.adapter = adapterPay
    }

    private fun setupRecyclerView() {
        adapter = ItemConfirmBuyAdapter(list)
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getDataFromIntent() {
        val style = intent.getStringExtra("style")

        if (style == "1") {
            styleOne()
        }else{
            styleTwo()
        }
    }
    private fun styleOne(){
        val url = intent.getStringExtra("url").orEmpty()
        val name = intent.getStringExtra("name").orEmpty()
        val color = intent.getStringExtra("color").orEmpty()
        val size = intent.getStringExtra("size").orEmpty()
        val price = intent.getStringExtra("price").orEmpty()
        val quantity = intent.getIntExtra("quantity",1)

        val product = ProductBuy(
            img = url,
            name = name,
            color = color,
            size = size,
            price = price,
            quantity = quantity
        )
        total = price.replace("k","", ignoreCase = true).trim().toInt() * quantity
        list.add(product)
        adapter.notifyDataSetChanged()
    }
    private fun styleTwo(){

    }
}
