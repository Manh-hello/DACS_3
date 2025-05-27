package com.example.project.Fragment.TabLayout

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
import com.example.project.Adapter.OrderAdapter
import com.example.project.Interface.ClickBTNOrder
import com.example.project.R
import com.example.project.ViewModel.OrderViewModel
import com.example.project.databinding.FormDialogInfoBinding
import com.example.project.databinding.FragmentDaHuyBinding
import com.example.projectmanage.Model.Entity.OrderDetail
import kotlin.getValue

class DaHuyFragment : Fragment(), ClickBTNOrder {
    private lateinit var binding: FragmentDaHuyBinding
    private val myViewModel : OrderViewModel  by viewModels{
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private lateinit var cusId: String
    private lateinit var adapter: OrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaHuyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.visibility = View.VISIBLE
        binding.rv.visibility = View.INVISIBLE
        binding.tv.visibility = View.INVISIBLE
        cusId = requireActivity().getSharedPreferences("id_customers", MODE_PRIVATE)
            .getString("id_customer", null).orEmpty()

        myViewModel.loadOrder(cusId)
        myViewModel.listOrder.observe(viewLifecycleOwner) { orders ->
            val ids = orders.map { it.id }
            myViewModel.loadOrderDetailCancel(ids)
        }
        myViewModel.mapOrderDetails.observe(viewLifecycleOwner) { map ->
            val orders = myViewModel.listOrder.value.orEmpty().filter { map.containsKey(it.id) }
            binding.progress.visibility = View.GONE
            adapter = OrderAdapter(orders.toMutableList(), map, this@DaHuyFragment)
            if (orders.isEmpty()) {
                binding.tv.visibility = View.VISIBLE
            } else {
                binding.apply {
                    rv.visibility = View.VISIBLE
                    rv.layoutManager = LinearLayoutManager(requireContext())
                    rv.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
    override fun clickSuccess(id: String){
        myViewModel.updateSuccess(id)
        adapter.notifyDataSetChanged()
    }
    override fun clickError(id: String)  {
        myViewModel.updateError(id)
        adapter.notifyDataSetChanged()
    }
    override fun clickInfo(item: OrderDetail){
        val dialogBinding = FormDialogInfoBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity(), R.style.dialog)
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.setCancelable(true)

        dialogBinding.apply {
            edtName.setText(item.name)
            edtPrice.setText(item.price)
            edtQuantity.setText(item.quantity.toString())
            edtColor.setText(item.color)
            edtSize.setText(item.size)
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
    override fun clickEvaluate(id: String) {
        TODO("Not yet implemented")
    }
}