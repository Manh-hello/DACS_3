package com.example.projectmanage.Fragment

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.projectmanage.Adapter.OrderAdapter
import com.example.projectmanage.Interface.ClickBTNOrder
import com.example.projectmanage.Model.Entity.OrderDetail
import com.example.projectmanage.R
import com.example.projectmanage.ViewModel.HomeViewModel
import com.example.projectmanage.databinding.FormDialogInfoBinding
import com.example.projectmanage.databinding.FragmentHomeBinding
import kotlin.getValue

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var storeId: String
    private val myViewModel : HomeViewModel by viewModels{
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private lateinit var adapter: OrderAdapter
    private var loadDone = 0
    var listOrderDt = mutableListOf<OrderDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeId = requireActivity().getSharedPreferences("id_store", MODE_PRIVATE)
            .getString("id_store", null).orEmpty()
        myViewModel.loadOrderDetail(storeId)
        myViewModel.getCount(storeId)
        myViewModel.count1.observe(viewLifecycleOwner){
            binding.tvSell.setText(it.toString())
            loadDone++
            checkLoadDone()
        }
        myViewModel.count2.observe(viewLifecycleOwner){
            binding.tvDelivery.setText(it.toString())
            loadDone++
            checkLoadDone()
        }
        myViewModel.count3.observe(viewLifecycleOwner){
            binding.tvTotal.setText(it.toString()+"k")
            loadDone++
            checkLoadDone()
        }

        myViewModel.orderDetails.observe (viewLifecycleOwner){list->
            listOrderDt.clear()
            listOrderDt.addAll(list)
            adapter.notifyDataSetChanged()
            loadDone++
            checkLoadDone()
        }
        adapter = OrderAdapter(listOrderDt,object : ClickBTNOrder{
            override fun clickSuccess(i: Int) {
                val item = listOrderDt[i]
                myViewModel.updateSuccess(item.id)
                myViewModel.loadOrderDetail(storeId)
                adapter.notifyDataSetChanged()
            }

            override fun clickError(i: Int) {
                val item = listOrderDt[i]
                myViewModel.updateError(item.id)
                myViewModel.loadOrderDetail(storeId)
                adapter.notifyDataSetChanged()
            }

            override fun clickInfo(i: Int) {
                val item = listOrderDt[i]
                val dialogBinding = FormDialogInfoBinding.inflate(layoutInflater)

                val builder = AlertDialog.Builder(requireActivity(), R.style.dialog)
                builder.setView(dialogBinding.root)
                val alertDialog = builder.create()
                alertDialog.setCancelable(true)

                dialogBinding.apply {
                    edtPrice.setText(item.price)
                    edtQuantity.setText(item.quantity.toString())
                    edtColor.setText(item.color)
                    edtSize.setText(item.size)
                    myViewModel.getName(item.orderId)
                    myViewModel.cusName.observe(viewLifecycleOwner){
                        edtCusName.setText(it.name)
                    }
                    Glide.with(img.context).load(item.src).into(img)
                    btnClose.setOnClickListener {
                        alertDialog.dismiss()
                    }
                    btnCl.setOnClickListener {
                        alertDialog.dismiss()
                    }
                }

                alertDialog.window?.setLayout(
                    (resources.displayMetrics.widthPixels * 0.9).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                alertDialog.show()
            }


        })
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(requireActivity())

    }
    private fun checkLoadDone() {
        loadDone++
        if (loadDone >= 4) {
            binding.layout.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
        }
    }
}