package com.example.project.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project.Adapter.ProductHomeAdapter
import com.example.project.Model.ProductFull
import com.example.project.R
import com.example.project.ViewModel.BuySuccessViewModel
import com.example.project.databinding.ActivityBuySuccessBinding

class BuySuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuySuccessBinding
    private val myViewModel : BuySuccessViewModel by viewModels()
    private lateinit var adapterProduct:ProductHomeAdapter
    var list2 = mutableListOf<ProductFull>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val proName = intent.getStringExtra("proName").orEmpty()

        myViewModel.loadProduct(proName)
        myViewModel.listProduct.observe(this, Observer{
            list2.clear()
            list2.addAll(it.filter { it.product.quantity>0 })
            adapterProduct.notifyDataSetChanged()
        })

        adapterProduct = ProductHomeAdapter(list2)
        binding.rv.layoutManager = GridLayoutManager(this,2)
        binding.rv.adapter = adapterProduct
        adapterProduct.setOnItemClickListener(object : ProductHomeAdapter.onItemClick{
            override fun onClick(i: Int) {
                val p = list2[i]
                val intent = Intent(this@BuySuccessActivity, ProductDetail::class.java)
                intent.putExtra("productFull",p)
                startActivity(intent)
            }
        })

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this@BuySuccessActivity, MainActivity::class.java))
        }

        binding.btnOrder.setOnClickListener {
            startActivity(Intent(this@BuySuccessActivity, OrderActivity::class.java))
        }

    }
}