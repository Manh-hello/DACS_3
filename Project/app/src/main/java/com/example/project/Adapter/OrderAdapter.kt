package com.example.project.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.Fragment.TabLayout.ChoXacNhanFragment
import com.example.project.Interface.ClickBTNOrder
import com.example.project.R
import com.example.project.databinding.RowOrderBinding
import com.example.projectmanage.Model.Entity.Order
import com.example.projectmanage.Model.Entity.OrderDetail

class OrderAdapter(
    private var ds: MutableList<Order>,
    private var mapDetail: Map<String, List<OrderDetail>>,
    private val onClick: ClickBTNOrder
) : RecyclerView.Adapter<OrderAdapter.RV>() {

    private val expandedState = mutableMapOf<String, Boolean>()

    inner class RV(private val binding: RowOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order, details: List<OrderDetail>) {
            val isExpanded = expandedState[item.id] ?: false

            binding.tvTotal.text = "Tổng tiền: ${item.pay}"

            val displayDetails = if (isExpanded) details else details.take(1)
            binding.rv.adapter = OrderItemAdapter(displayDetails.toMutableList(),onClick)
            binding.rv.layoutManager = LinearLayoutManager(binding.root.context)

            if (isExpanded) {
                binding.btnDown.setImageResource(R.drawable.ic_up)
            } else {
                binding.btnDown.setImageResource(R.drawable.ic_down)
            }

            binding.btnDown.visibility = if (details.size > 1) View.VISIBLE else View.GONE

            binding.btnDown.setOnClickListener {
                expandedState[item.id] = !isExpanded
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun onBindViewHolder(holder: RV, position: Int) {
        val order = ds[position]
        val details = mapDetail[order.id] ?: emptyList()
        holder.bind(order, details)
    }

    override fun getItemCount() = ds.size

    fun updateData(newDs: List<Order>, newMap: Map<String, List<OrderDetail>>) {
        ds.clear()
        ds.addAll(newDs)
        mapDetail = newMap
        expandedState.clear()
        notifyDataSetChanged()
    }

}
