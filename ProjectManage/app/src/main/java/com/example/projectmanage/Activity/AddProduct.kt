package com.example.projectmanage.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanage.Adapter.ListImgProduct
import com.example.projectmanage.ViewModel.ProductViewModel
import com.example.projectmanage.databinding.ActivityAddProductBinding

class AddProduct : AppCompatActivity() {

    private val myViewModel: ProductViewModel by viewModels()
    private lateinit var binding: ActivityAddProductBinding
    private val listUri = mutableListOf<Uri>()
    private lateinit var storeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeId = getSharedPreferences("id_store", MODE_PRIVATE)
            .getString("id_store", null).orEmpty()

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvListImg.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListImg.adapter = ListImgProduct(listUri)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }


        binding.btnSelect.setOnClickListener { openImagePicker() }

        binding.btnAdd.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val price = binding.edtPrice.text.toString().trim()
            val quantity = binding.edtQuantity.text.toString().trim()
            val desc = binding.edtDesc.text.toString().trim()
            val color = binding.edtColor.text.toString().trim()
            val size = binding.edtSize.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() ||
                desc.isEmpty() || listUri.isEmpty()) {
                Toast.makeText(this, "Bạn chưa điền đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                myViewModel.addProduct(storeId, name, price, quantity, desc, color, size, listUri)
            }
        }
        myViewModel.isSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        })

    }

    private fun observeViewModel() {
        myViewModel.isSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        myViewModel.error.observe(this, Observer{error->
            Toast.makeText(this,error, Toast.LENGTH_SHORT).show()
        })
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"))
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            listUri.clear()
            result.data?.let { data ->
                data.clipData?.let { clipData ->
                    for (i in 0 until clipData.itemCount) {
                        listUri.add(clipData.getItemAt(i).uri)
                    }
                } ?: data.data?.let {
                    listUri.add(it)
                }
                binding.rvListImg.adapter = ListImgProduct(listUri)
            }
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
