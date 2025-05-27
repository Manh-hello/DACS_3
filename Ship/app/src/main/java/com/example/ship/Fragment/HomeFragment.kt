package com.example.ship.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ship.Adapter.ListOrderAdapter
import com.example.ship.Interface.OnClickOrder
import com.example.ship.Model.OrderDetail
import com.example.ship.R
import com.example.ship.ViewModel.HomeViewModel
import com.example.ship.databinding.FormDialogInfoBinding
import com.example.ship.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var list = mutableListOf<OrderDetail>()
    private val myViewModel: HomeViewModel by viewModels{
        SavedStateViewModelFactory(requireActivity().application, this)
    }
    private lateinit var adapter: ListOrderAdapter
    private lateinit var shiperId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shareRef = requireActivity().getSharedPreferences("id_shiper", AppCompatActivity.MODE_PRIVATE)
        shiperId = shareRef.getString("id_shiper",null).toString()

        adapter = ListOrderAdapter(list,object : OnClickOrder{
            override fun info(i: Int) {
                val item = list[i]
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
                    myViewModel.getName(item.orderId)
                    myViewModel.cusName.observe(viewLifecycleOwner){
                        edtCusName.setText(it.name)
                        edtAddress.setText(it.address)
                    }
                    Glide.with(img.context).load(item.src).into(img)

                    btnClose.setOnClickListener {
                        alertDialog.dismiss()
                    }
                    btnCl.setOnClickListener {
                        myViewModel.get(item.id, shiperId)
                        myViewModel.selectAllOrder()
                        adapter.notifyDataSetChanged()
                        alertDialog.dismiss()
                    }
                }

                alertDialog.window?.setLayout(
                    (resources.displayMetrics.widthPixels * 0.9).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                alertDialog.show()
            }

            override fun select(i: Int) {
                myViewModel.get(list[i].id, shiperId)
                myViewModel.selectAllOrder()
                adapter.notifyDataSetChanged()
            }
        })
        myViewModel.selectAllOrder()
        myViewModel.listOrder.observe(viewLifecycleOwner){
            list.clear()
            list.addAll(it.filter { it.status == "Đã xác nhận" })
            binding.rv.layoutManager = LinearLayoutManager(requireActivity())
            binding.rv.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}