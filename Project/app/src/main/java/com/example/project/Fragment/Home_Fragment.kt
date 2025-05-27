package com.example.project.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.Adapter.NavHomeAdapter
import com.example.project.R
import com.example.project.ViewModel.HomeViewModel
import com.example.project.databinding.FragmentHomeBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.project.Activity.CartActivity
import com.example.project.Activity.DiscountActivity
import com.example.project.Activity.FindProduct
import com.example.project.Activity.OrderActivity
import com.example.project.Activity.ProductDetail
import com.example.project.Activity.ResultFindActivity
import com.example.project.Adapter.ProductHomeAdapter
import com.example.project.Adapter.SlideAdapter
import com.example.project.Interface.SelectNav
import com.example.project.Model.Nav_Home
import com.example.project.Model.ProductFull
import com.example.project.Model.SlideModel

class Home_Fragment : Fragment() {
    private val myViewModel: HomeViewModel by viewModels{
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterNav:NavHomeAdapter
    private lateinit var slideAdapter: SlideAdapter
    private val slideList = mutableListOf<SlideModel>()
    private lateinit var adapterProduct:ProductHomeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var list1 = mutableListOf<Nav_Home>()
        var list2 = mutableListOf<ProductFull>()
        var progressBar = view.findViewById<TextView>(R.id.tvProgress)

        adapterNav = NavHomeAdapter(list1,object : SelectNav{
            override fun select(nav: String) {
                if(nav=="Mã Giảm Giá"){
                    startActivity(Intent(requireActivity(), DiscountActivity::class.java))
                }else if(nav=="Sản Phẩm Bán Chạy"){

                }else if(nav=="Khuyến Mãi"){

                }else if(nav=="Đơn Hàng Của Tôi"){
                    startActivity(Intent(requireActivity(), OrderActivity::class.java))
                }else{

                }
            }

        })
        myViewModel.loadNav()
        myViewModel.nav.observe(viewLifecycleOwner){ item->
            list1.clear()
            list1.addAll(item)
            adapterNav.notifyDataSetChanged()
        }
        binding.navRv.layoutManager  = LinearLayoutManager(
                requireActivity(),
        LinearLayoutManager.HORIZONTAL,
        false
        )
        binding.navRv.adapter = adapterNav

        binding.navRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var x1 = recyclerView.computeHorizontalScrollExtent() // Kích thước phần hiển thị
                var x2 = recyclerView.computeHorizontalScrollRange() // Tổng chiều dài nội dung
                var x = recyclerView.computeHorizontalScrollOffset() // Vị trí cuộn hiện tại
                // Tính phần trăm thanh cuộn
                val progress = (x.toFloat() / (x2 - x1)*25).toInt()
                progressBar.translationX = progress.toFloat() // Di chuyển thanh trượt
            }
        })
        slideAdapter = SlideAdapter(slideList, binding.vp2)

        binding.vp2.adapter = slideAdapter
        binding.vp2.clipToPadding = false
        binding.vp2.clipChildren = false
        binding.vp2.offscreenPageLimit = 3
        binding.vp2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.vp2.setPageTransformer(compositePageTransformer)

        slideAdapter.setOnItemClickListener(object : SlideAdapter.onItemClick {
            override fun onClick(text: String) {
                val intent = Intent(requireActivity(), ResultFindActivity::class.java)
                intent.putExtra("text", text)
                startActivity(intent)
            }
        })
        initBanner()

        myViewModel.loadProduct()
        myViewModel.listProduct.observe(viewLifecycleOwner){item->
            list2.clear()
            list2.addAll(item.filter { it.product.quantity > 0 })
            adapterProduct.notifyDataSetChanged()
        }
        adapterProduct = ProductHomeAdapter(list2)
        binding.rvProductHot.layoutManager = GridLayoutManager(requireActivity(),2)
        binding.rvProductHot.adapter = adapterProduct
        adapterProduct.setOnItemClickListener(object : ProductHomeAdapter.onItemClick{
            override fun onClick(i: Int) {
                val p = list2[i]
                val intent = Intent(requireActivity(), ProductDetail::class.java)
                intent.putExtra("productFull",p)
                startActivity(intent)
            }
        })

        binding.edtFind.setOnClickListener {
            startActivity(Intent(requireActivity(),FindProduct::class.java))
        }
        binding.btnCart.setOnClickListener {
            startActivity(Intent(requireActivity(),CartActivity::class.java))
        }
    }
    private fun initBanner() {
        myViewModel.loadBanners()
        myViewModel.banners.observe(viewLifecycleOwner) { banners ->
            slideList.clear()
            slideList.addAll(banners)
            slideAdapter.notifyDataSetChanged()

            // Hiển thị dot indicator nếu có nhiều hơn 1 banner
            binding.dotIndicator.visibility = if (banners.size > 1) View.VISIBLE else View.GONE
            if (banners.size > 1) {
                binding.dotIndicator.attachTo(binding.vp2)
            }
        }
    }

}
