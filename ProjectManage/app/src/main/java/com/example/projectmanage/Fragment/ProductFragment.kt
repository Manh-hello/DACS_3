package com.example.projectmanage.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanage.Activity.AddProduct
import com.example.projectmanage.Activity.FixProduct
import com.example.projectmanage.Adapter.ListProduct
import com.example.projectmanage.Model.ProductFull
import com.example.projectmanage.R
import com.example.projectmanage.ViewModel.ProductViewModel
import com.example.projectmanage.databinding.FragmentProductBinding

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var adapter: ListProduct
    private val myViewModel: ProductViewModel by viewModels()
    private var listPro = mutableListOf<ProductFull>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progress.visibility = View.VISIBLE
        binding.rvList.visibility = View.GONE
        binding.not.visibility = View.GONE

        adapter = ListProduct(listPro) { product ->
            val intent = Intent(requireContext(), FixProduct::class.java)
            intent.putExtra("proFull", product)
            fixProductLauncher.launch(intent)
        }
        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())

        myViewModel.listProduct.observe(viewLifecycleOwner, Observer { productList ->
            listPro.clear()
            listPro.addAll(productList)
            adapter.notifyDataSetChanged()

            if (productList.isEmpty()) {
                binding.not.visibility = View.VISIBLE
                binding.rvList.visibility = View.GONE
            } else {
                binding.not.visibility = View.GONE
                binding.rvList.visibility = View.VISIBLE
            }
            binding.progress.visibility = View.GONE
        })

        val storeId = requireActivity()
            .getSharedPreferences("id_store", android.content.Context.MODE_PRIVATE)
            .getString("id_store", null).orEmpty()

        if (storeId.isNotEmpty()) {
            myViewModel.selectProduct(storeId)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(requireActivity(), AddProduct::class.java)
            addProductLauncher.launch(intent)
        }
    }

    private val addProductLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val storeId = requireActivity()
                .getSharedPreferences("id_store", android.content.Context.MODE_PRIVATE)
                .getString("id_store", null).orEmpty()
            if (storeId.isNotEmpty()) {
                binding.progress.visibility = View.VISIBLE
                binding.rvList.visibility = View.GONE
                binding.not.visibility = View.GONE
                myViewModel.selectProduct(storeId)
            }
        }
    }
    private val fixProductLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val storeId = requireActivity()
                .getSharedPreferences("id_store", android.content.Context.MODE_PRIVATE)
                .getString("id_store", null).orEmpty()
            if (storeId.isNotEmpty()) {
                binding.progress.visibility = View.VISIBLE
                binding.rvList.visibility = View.GONE
                binding.not.visibility = View.GONE
                myViewModel.selectProduct(storeId)
            }
        }
    }
}
