package com.example.project.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.Adapter.FindAdapter
import com.example.project.Model.ProductFull
import com.example.project.R
import com.example.project.ViewModel.FindViewModel
import com.example.project.databinding.ActivityFindProductBinding

class FindProduct : AppCompatActivity() {

    private lateinit var binding: ActivityFindProductBinding
    private lateinit var adapter: FindAdapter
    private val myViewModel: FindViewModel by viewModels()
    private val listProSuggest = mutableListOf<ProductFull>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
        showKeyboardOnStart()
    }

    private fun setupRecyclerView() {
        adapter = FindAdapter(listProSuggest)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnFind.setOnClickListener {
            val keyword = binding.edtFind.text.toString().trim()
            if (keyword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập sản phẩm cần tìm kiếm", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, ResultFindActivity::class.java)
            intent.putExtra("text", keyword)
            startActivity(intent)
            finish()
        }

        binding.edtFind.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                myViewModel.setSearchQuery(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        adapter.setOnItemClickListener(object : FindAdapter.onItemClick {
            override fun onClick(i: Int) {
                val p = listProSuggest[i]
                val intent = Intent(this@FindProduct, ProductDetail::class.java)
                intent.putExtra("productFull",p)
                startActivity(intent)
            }
        })
    }

    private fun observeViewModel() {
        myViewModel.listProFull.observe(this) { list ->
            listProSuggest.clear()
            listProSuggest.addAll(list.filter { it.product.quantity>0 })
            adapter.notifyDataSetChanged()

            if (list.isEmpty()) {
                binding.rv.visibility = View.INVISIBLE
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.INVISIBLE
                binding.rv.visibility = View.VISIBLE
            }
        }
    }

    private fun showKeyboardOnStart() {
        Handler(Looper.getMainLooper()).post {
            binding.edtFind.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.edtFind, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}
