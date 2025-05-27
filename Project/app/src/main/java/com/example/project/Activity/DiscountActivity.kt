package com.example.project.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.Adapter.ItemDiscountAdapter
import com.example.project.Interface.SelectDiscount
import com.example.project.Model.Entity.MyDiscount
import com.example.project.R
import com.example.project.ViewModel.DiscountViewModel
import com.example.project.databinding.ActivityDiscountBinding
import com.example.projectmanage.Model.Entity.Discount

class DiscountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscountBinding
    private val myViewModel : DiscountViewModel by viewModels()
    private var list = mutableListOf<Discount>()
    private var list2 = mutableListOf<MyDiscount>()
    private lateinit var adapter: ItemDiscountAdapter
    private lateinit var cusId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        getCustomerId()
        fetchDiscounts()
        observeViewModel()
        setupAdapter()
        setupRecyclerView()
        handleEvents()
    }

    private fun initUI() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
    }

    private fun getCustomerId() {
        cusId = getSharedPreferences("id_customers", MODE_PRIVATE)
            .getString("id_customer", null).orEmpty()
    }

    private fun observeViewModel() {
        myViewModel.list2.observe(this, Observer { listAll ->
            list2.clear()
            list2.addAll(listAll)
            adapter.notifyDataSetChanged()
        })

        myViewModel.error.observe(this, Observer {
            Toast.makeText(this@DiscountActivity, "$it", Toast.LENGTH_SHORT).show()
            Log.d("error", "$it")
        })

        myViewModel.list.observe(this, Observer { listAll ->
            list.clear()
            list.addAll(listAll)
            adapter.notifyDataSetChanged()
            binding.rv.visibility = View.VISIBLE
            binding.progress.visibility = View.INVISIBLE
        })
    }

    private fun setupAdapter() {
        adapter = ItemDiscountAdapter(list, list2, object : SelectDiscount {
            override fun onClick(i: Int) {
                val disId = list[i].id
                val reduce = list[i].reduce
                val code = list[i].code

                myViewModel.save(cusId, disId, code, reduce){
                    myViewModel.select(cusId)
                    myViewModel.selectAll()
                }

            }
        })
    }

    private fun setupRecyclerView() {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchDiscounts() {
        myViewModel.selectAll()
        myViewModel.select(cusId)
    }

    private fun handleEvents() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
