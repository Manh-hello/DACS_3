package com.example.projectmanage.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanage.Activity.AddDisCount
import com.example.projectmanage.Activity.FixDiscount
import com.example.projectmanage.Adapter.ListDiscount
import com.example.projectmanage.Adapter.ListProduct
import com.example.projectmanage.Model.Entity.Discount
import com.example.projectmanage.R
import com.example.projectmanage.ViewModel.DiscountViewModel
import com.example.projectmanage.ViewModel.ProductViewModel
import com.example.projectmanage.databinding.FragmentDiscountBinding
import kotlin.getValue

class DiscountFragment : Fragment() {
    private lateinit var binding: FragmentDiscountBinding
    private lateinit var adapter: ListDiscount
    private val myViewModel: DiscountViewModel by viewModels()
    private var listDiscount=mutableListOf<Discount>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progress.visibility = View.VISIBLE
        binding.rvList.visibility = View.INVISIBLE
        binding.not.visibility = View.INVISIBLE

        val storeId = requireActivity()
            .getSharedPreferences("id_store", android.content.Context.MODE_PRIVATE)
            .getString("id_store", null).orEmpty()
        adapter = ListDiscount(listDiscount){discount->
            val intent = Intent(requireActivity(), FixDiscount::class.java)
            intent.putExtra("discount",discount)
            fixDiscountLauncher.launch(intent)
        }
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvList.adapter = adapter
        myViewModel.listDiscount.observe(viewLifecycleOwner) { discountList ->
            listDiscount.clear()
            listDiscount.addAll(discountList)
            adapter.notifyDataSetChanged()

            binding.progress.visibility = View.INVISIBLE

            if (discountList.isEmpty()) {
                binding.not.visibility = View.VISIBLE
                binding.rvList.visibility = View.INVISIBLE
            } else {
                binding.not.visibility = View.INVISIBLE
                binding.rvList.visibility = View.VISIBLE
            }
        }

        if (storeId.isNotEmpty()) {
            myViewModel.selectAll(storeId)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(requireActivity(), AddDisCount::class.java)
            addDiscountLauncher.launch(intent)
        }
    }

    private val addDiscountLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val storeId = requireActivity()
                .getSharedPreferences("id_store", android.content.Context.MODE_PRIVATE)
                .getString("id_store", null).orEmpty()
            if (storeId.isNotEmpty()) {
                binding.progress.visibility = View.VISIBLE
                binding.rvList.visibility = View.INVISIBLE
                binding.not.visibility = View.INVISIBLE
                myViewModel.selectAll(storeId)
            }
        }
    }
    private val fixDiscountLauncher = registerForActivityResult(
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
                myViewModel.selectAll(storeId)
            }
        }
    }
}
