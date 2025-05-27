package com.example.project.Fragment.TabLayout

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project.Adapter.OrderAdapter
import com.example.project.Interface.ClickBTNOrder
import com.example.project.R
import com.example.project.ViewModel.BellViewModel
import com.example.project.ViewModel.OrderViewModel
import com.example.project.databinding.FormDialogInfoBinding
import com.example.project.databinding.FragmentChoXacNhanBinding
import com.example.projectmanage.Model.Entity.OrderDetail
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.getValue

class ChoXacNhanFragment : Fragment(), ClickBTNOrder {
    private lateinit var binding: FragmentChoXacNhanBinding
    private val myViewModel: OrderViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private val bellViewModel : BellViewModel by viewModels{
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private lateinit var cusId: String
    private lateinit var adapter: OrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoXacNhanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.visibility = View.VISIBLE
        binding.rv.visibility = View.INVISIBLE
        binding.tv.visibility = View.INVISIBLE
        adapter = OrderAdapter(mutableListOf(), mutableMapOf(), this@ChoXacNhanFragment)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter

        cusId = requireActivity().getSharedPreferences("id_customers", MODE_PRIVATE)
            .getString("id_customer", null).orEmpty()

        myViewModel.loadOrder(cusId)
        myViewModel.listOrder.observe(viewLifecycleOwner) { orders ->
            val ids = orders.map { it.id }
            myViewModel.loadOrderDetailWait(ids)
        }

        myViewModel.mapOrderDetails.observe(viewLifecycleOwner) { map ->
            binding.progress.visibility = View.GONE

            val orders = myViewModel.listOrder.value
                .orEmpty()
                .filter { map.containsKey(it.id) }

            if (orders.isEmpty()) {
                binding.tv.visibility = View.VISIBLE
                binding.rv.visibility = View.GONE
            } else {
                binding.tv.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
                adapter.updateData(orders, map)
            }
        }
    }
    override fun clickSuccess(id: String){
        myViewModel.updateSuccess(id)
        myViewModel.listOrder.value
            ?.map { it.id }
            ?.let { myViewModel.loadOrderDetailWait(it) }
    }
    override fun clickError(id: String)  {
        myViewModel.updateError(id)
        myViewModel.loadOrder(cusId)
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formartDate = format.format(calendar.time)
        bellViewModel.save(cusId,"Hủy","Hủy đơn hàng thành công","Bạn đã hủy đơn hàng thành công",formartDate)
        myViewModel.listOrder.value?.map { it.id }?.let { myViewModel.loadOrderDetailWait(it) }
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