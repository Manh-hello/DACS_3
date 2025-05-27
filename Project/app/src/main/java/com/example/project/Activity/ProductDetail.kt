package com.example.project.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.project.Adapter.EvaluateAdapter
import com.example.project.Adapter.ListPicAdapter
import com.example.project.Adapter.SlideProductAdapter
import com.example.project.Model.Entity.Customer
import com.example.project.Model.ProductFull
import com.example.project.R
import com.example.project.ViewModel.BellViewModel
import com.example.project.ViewModel.CartViewModel
import com.example.project.ViewModel.OrderViewModel
import com.example.project.ViewModel.ProductViewModel
import com.example.project.databinding.ActivityProductDetailBinding
import com.example.projectmanage.Model.Entity.Evaluate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProductDetail : AppCompatActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private val bellViewModel: BellViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var listPicAdapter: ListPicAdapter
    private lateinit var slideProductAdapter: SlideProductAdapter
    private var listImg = mutableListOf<String>()
    private var listSize = mutableListOf<String>()
    private var listColor = mutableListOf<String>()
    private val listEvaluate = mutableListOf<Evaluate>()
    private lateinit var proId: String
    private lateinit var cusId: String
    private lateinit var storeId: String
    private lateinit var cust: Customer
    private var isUserScroll = true
    private var proQtt = 0
    private lateinit var adapter: EvaluateAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()
        setupListeners()

        adapter = EvaluateAdapter(listEvaluate)
        binding.rvEvaluate.layoutManager = LinearLayoutManager(this)
        binding.rvEvaluate.adapter = adapter

        intent.getParcelableExtra<ProductFull>("productFull")?.let { product ->
            productViewModel.init(product)
            bindProductInfo(product)
            productViewModel.selectEvaluate(product.product.id)
        }
    }

    private fun setupUI() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        binding.listPic.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun setupObservers() {
        productViewModel.pic.observe(this) { pics ->
            listImg.clear()
            listImg.addAll(pics)
            setupImageSlider()
            setupImage()
        }
        productViewModel.size.observe(this) { sizes ->
            listSize.clear()
            listSize.addAll(sizes)
            setupSizeSpinner()
        }
        productViewModel.color.observe(this) { colors ->
            listColor.clear()
            listColor.addAll(colors)
            setupColorSpinner()
        }
        productViewModel.listEvaluate.observe(this, Observer{
            listEvaluate.clear()
            listEvaluate.addAll(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnCart.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
        binding.btnSub.setOnClickListener { updateQuantity(-1) }
        binding.btnAdd.setOnClickListener { updateQuantity(1) }
        binding.btnAddCart.setOnClickListener {
            cartViewModel.loadInfo(cusId)
            cartViewModel.cust.observe(this, Observer {
                if (it != null) {
                    cust = it
                    if (binding.tvCount.text.toString().toInt() > proQtt) {
                        Toast.makeText(this, "không đủ số lượng trong kho, số lượng trong kho là $proQtt, bạn có thể giảm số lượng còn $proQtt", Toast.LENGTH_SHORT).show()
                    } else if (it.address == "") {
                        Toast.makeText(this, "Bạn chưa khai báo địa chỉ", Toast.LENGTH_SHORT).show()
                    } else {
                        addToCart()

                    }
                }
            })
        }
        binding.btnBell.setOnClickListener { notification() }
        binding.btnBuy.setOnClickListener {
            cartViewModel.loadInfo(cusId)
            cartViewModel.cust.observe(this, Observer {
                if (it != null) {
                    cust = it
                    if (binding.tvCount.text.toString().toInt() > proQtt) {
                        Toast.makeText(this, "không đủ số lượng trong kho, số lượng trong kho là $proQtt", Toast.LENGTH_SHORT).show()
                    } else if (it.address == "") {
                        Toast.makeText(this, "Bạn chưa khai báo địa chỉ", Toast.LENGTH_SHORT).show()
                    } else {
                        confirmBuy()
                    }
                }
            })
        }
        binding.avatar.setOnClickListener {
            chat()
        }
        binding.namePerson.setOnClickListener {
            chat()
        }
    }

    private fun bindProductInfo(product: ProductFull) {
        binding.apply {
            tvTitle.text = product.product.name
            tvDecripton.text = product.product.desc
            tvPrice.text = product.product.price
            countPay.text = product.product.count.toString()
            tvEvaluate.text = product.product.star.toString()
            proId = product.product.id ?: ""
            proQtt = product.product.quantity
            cusId = getSharedPreferences("id_customers", MODE_PRIVATE)
                .getString("id_customer", null).orEmpty()
            productViewModel.getStore(product.product.storeId)
            productViewModel.storeInfo.observe(this@ProductDetail, Observer{
                if (it.img.isNotEmpty()){
                    Glide.with(avatar).load(it.img).into(avatar)
                }
                namePerson.setText(it.name)
                storeId = it.id
                binding.container.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            })
        }
    }

    private fun setupImageSlider() {
        slideProductAdapter = SlideProductAdapter(listImg, binding.imgProduct)
        binding.imgProduct.apply {
            adapter = slideProductAdapter
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
            })

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (isUserScroll) {
                        listPicAdapter.updateSelectedPosition(position)
                        binding.listPic.scrollToPosition(position)
                    }
                }
            })
        }
    }

    private fun setupImage() {
        listPicAdapter = ListPicAdapter(listImg)
        binding.listPic.adapter = listPicAdapter
        listPicAdapter.setOnItemClickListener(object : ListPicAdapter.onItemClick {
            override fun onClick(position: Int) {
                isUserScroll = false
                binding.imgProduct.setCurrentItem(position, true)
                isUserScroll = true
            }
        })
    }

    private fun setupSizeSpinner() {
        binding.spnSize.adapter = ArrayAdapter(this, R.layout.row_select_discount, listSize)
        binding.spnSize.setSelection(0)
    }

    private fun setupColorSpinner() {
        binding.spnColor.adapter = ArrayAdapter(this, R.layout.row_select_discount, listColor)
        binding.spnColor.setSelection(0)
    }

    private fun updateQuantity(amount: Int) {
        val current = binding.tvCount.text.toString().toIntOrNull() ?: 1
        val newCount = (current + amount).coerceAtLeast(1)
        binding.tvCount.text = newCount.toString()
    }

    private fun addToCart() {
        val selectedSize = binding.spnSize.selectedItem.toString()
        val selectedColor = binding.spnColor.selectedItem.toString()
        val quantity = binding.tvCount.text.toString()
        val price = binding.tvPrice.text.toString()
        val name = binding.tvTitle.text.toString()
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formartDate = format.format(calendar.time)
        productViewModel.addCart(
            cusId,
            proId,
            name,
            listImg[0],
            selectedSize,
            selectedColor,
            quantity,
            price
        )
        productViewModel.success.observe(this, Observer {
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
            bellViewModel.save(
                cusId,
                "Add Cart",
                "Thêm vào giỏ hàng thành công",
                "Bạn đã thêm sản phẩm này vào giỏ hàng thành công",
                formartDate
            )
        })
        productViewModel.error.observe(this, Observer {
            Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show()
        })
    }

    private fun notification() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            putExtra("fragment", "fragment_bell")
        })
        finish()
    }

    private fun confirmBuy() {
        val id = productViewModel.product.value?.product?.id ?: ""
        val quantity = binding.tvCount.text.toString().toIntOrNull() ?: 1
        val name = binding.tvTitle.text.toString()
        val size = binding.spnSize.selectedItem.toString()
        val color = binding.spnColor.selectedItem.toString()
        val price = binding.tvPrice.text.toString()
        startActivity(Intent(this, ConfirmBuy::class.java).apply {
            putExtra("style", "1")
            putExtra("proId", id)
            putExtra("url", listImg[0])
            putExtra("quantity", quantity)
            putExtra("name", name)
            putExtra("size", size)
            putExtra("color", color)
            putExtra("price", price)
        })
    }

    private fun chat(){
        val inten = Intent(this,ChatActivity::class.java)
        inten.putExtra("storeId",storeId)
        startActivity(inten)
    }
}
