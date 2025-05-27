package com.example.project.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project.Adapter.TabLayoutAdapter
import com.example.project.R
import com.example.project.databinding.ActivityOrderBinding
import com.google.android.material.tabs.TabLayoutMediator

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var adapter: TabLayoutAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TabLayoutAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Chờ xác nhận"
                1 -> tab.text = "Đã xác nhận"
                2 -> tab.text = "Đang giao hàng"
                3 -> tab.text = "Đã nhận hàng"
                else -> tab.text = "Đã hủy"
            }
        }.attach()

        val selectedTab = intent.getIntExtra("selected_tab", 0)
        binding.viewPager.currentItem = selectedTab

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}