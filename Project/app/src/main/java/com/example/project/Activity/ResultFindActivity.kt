package com.example.project.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project.Adapter.ProductHomeAdapter
import com.example.project.Model.ProductFull
import com.example.project.R
import com.example.project.ViewModel.FindViewModel
import com.example.project.ViewModel.HomeViewModel
import com.example.project.databinding.ActivityResultFindBinding

class ResultFindActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultFindBinding
    private lateinit var adapter: ProductHomeAdapter
    private val myViewModel: FindViewModel by viewModels()
    private val listProSuggest = mutableListOf<ProductFull>()
    private lateinit var bf: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultFindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var text = intent.getStringExtra("text").orEmpty()
        binding.edtFind.setText(text.toString().trim())

        myViewModel.setSearchQuery(text)
        myViewModel.listProFull.observe(this) { list ->
            bf = text
            listProSuggest.clear()
            listProSuggest.addAll(list.filter { it.product.quantity > 0 })
            adapter.notifyDataSetChanged()

            if (list.isEmpty()) {
                binding.rv.visibility = View.INVISIBLE
                binding.progress.visibility = View.VISIBLE
                binding.progress1.visibility = View.INVISIBLE
            } else {
                binding.progress.visibility = View.INVISIBLE
                binding.progress1.visibility = View.INVISIBLE
                binding.rv.visibility = View.VISIBLE
            }
        }
        binding.edtFind.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                text = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.btnFind.setOnClickListener {
            if (bf != text) {
                binding.rv.visibility = View.INVISIBLE
                binding.progress.visibility = View.INVISIBLE
                binding.progress1.visibility = View.VISIBLE
            }
            if (text.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            myViewModel.setSearchQuery(text.toString().trim())

        }
        binding.btnBack.setOnClickListener { finish() }

        adapter = ProductHomeAdapter(listProSuggest)
        binding.rv.layoutManager = GridLayoutManager(this, 2)
        binding.rv.adapter = adapter
        adapter.setOnItemClickListener(object : ProductHomeAdapter.onItemClick {
            override fun onClick(i: Int) {
                val p = listProSuggest[i]
                val intent = Intent(this@ResultFindActivity, ProductDetail::class.java)
                intent.putExtra("productFull", p)
                startActivity(intent)
            }
        })


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