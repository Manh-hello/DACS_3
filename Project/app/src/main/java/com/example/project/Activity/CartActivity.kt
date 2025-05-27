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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.Adapter.ItemCartAdapter
import com.example.project.Interface.ActiveCount
import com.example.project.Model.Entity.Cart
import com.example.project.Model.Entity.Customer
import com.example.project.Model.Entity.MyDiscount
import com.example.project.R
import com.example.project.ViewModel.BellViewModel
import com.example.project.ViewModel.CartViewModel
import com.example.project.databinding.ActivityCartBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CartActivity : AppCompatActivity() {
    private val myViewModel: CartViewModel by viewModels()
    private val bellViewModel: BellViewModel by viewModels()
    private lateinit var binding: ActivityCartBinding
    var listItem: MutableList<Cart> = mutableListOf<Cart>()
    var list1: MutableList<MyDiscount> = mutableListOf<MyDiscount>()
    private lateinit var adapter: ItemCartAdapter
    private lateinit var cusId: String
    private lateinit var cust: Customer
    private var total = 0
    private var reduce = 0
    private var pay = 0
    private lateinit var disId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)

        cusId = getSharedPreferences("id_customers", MODE_PRIVATE)
            .getString("id_customer", null).orEmpty()

        binding.btnBack.setOnClickListener {
            finish()
        }
        load()
        binding.btnBuy.setOnClickListener {
            if (listItem.size > 0) {
                myViewModel.loadInfo(cusId)
                myViewModel.cust.observe(this, Observer {
                    if (it != null) {
                        cust = it
                        if (it.address == "") {
                            Toast.makeText(this, "Bạn chưa khai báo địa chỉ", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            if (binding.spnPay.selectedItem.toString() == "Tiền mặt") {
                                handle1()
                            } else if (binding.spnPay.selectedItem.toString() == "Thanh toán qua Momo") {
                                handle2()
                            }
                        }
                        myViewModel.cust.removeObservers(this)
                    }
                })
            }
        }
    }

    fun handle1(){
        if (reduce != 0) {
            myViewModel.updateMyDiscount(disId)
        }
        myViewModel.clear(cusId)
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formartDate = format.format(calendar.time)

        myViewModel.saveMoreOne(
            cusId,
            "${reduce}k",
            "${pay}k",
            "${total}k",
            listItem,
            formartDate
        )
        bellViewModel.save(
            cusId,
            "Đặt Hàng",
            "Đặt hàng thanh công",
            "Bạn đã đặt hàng thành công, đơn hàng sẽ sớm giao đến bạn",
            formartDate
        )
        myViewModel.success.observe(this, Observer {
            startActivity(Intent(this, BuySuccessActivity::class.java))
        })
    }

    fun handle2(){
        val i = Intent(this, BuyOnlineActivity::class.java)
        i.putExtra("type", "2")
        i.putExtra("cusId", cusId)
        i.putExtra("pay", binding.tvPay.text)
        i.putExtra("reduce",reduce)
        i.putExtra("total",total)
        i.putExtra("list", ArrayList(listItem))
        i.putExtra("disId", disId)
        startActivity(i)
    }

    fun load() {
        adapter = ItemCartAdapter(listItem, object : ActiveCount {
            override fun sub(i: Int) {
                myViewModel.subQuantity(listItem[i].id)
                myViewModel.loadList(cusId)
            }

            override fun add(i: Int) {
                myViewModel.addQuantity(listItem[i].id)
                myViewModel.loadList(cusId)
            }

            override fun delete(i: Int) {
                myViewModel.delete(listItem[i].id)
                myViewModel.loadList(cusId)
            }
        })
        myViewModel.loadList(cusId)
        myViewModel.loadDiscount(cusId)
        val listPay = listOf("Tiền mặt", "Thanh toán qua Momo")
        val adapterPay = ArrayAdapter(this, R.layout.row_select_discount, listPay)
        binding.spnPay.adapter = adapterPay
        myViewModel.listItem.observe(this) { items ->
            listItem.clear()
            listItem.addAll(items)
            total = 0
            for (list in listItem) {
                val t =
                    list.price.replace("k", "", ignoreCase = true).trim()
                        .toInt() * list.quantity
                total += t
            }
            pay = total - reduce
            if (pay < 0) {
                pay = 0
            }
            binding.tvTotal.text = "${total}k"
            binding.tvPay.text = "${pay}k"
            adapter.notifyDataSetChanged()
            binding.layout.visibility = View.VISIBLE
            binding.progress.visibility = View.INVISIBLE
        }
        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.adapter = adapter
        myViewModel.list1.observe(this) { items ->
            list1.clear()
            val emptyDiscount = MyDiscount("", "", "", "", "0k", true)
            list1.add(emptyDiscount)
            val validDiscounts = items.filter { it.status == true }
            list1.addAll(validDiscounts)

            val discountCodes = list1.map { it.reduce }
            val adapterDiscount =
                ArrayAdapter(this, R.layout.row_select_discount, discountCodes)
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
                        disId = selectedDiscount.id
                        reduce =
                            selectedDiscount.reduce.replace("k", "", ignoreCase = true).trim()
                                .toInt()
                        binding.tvDiscount.text = selectedDiscount.reduce
                        pay = total - reduce
                        if (pay < 0) {
                            pay = 0
                        }
                        binding.tvPay.text = "${pay}k"
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }
    }
}