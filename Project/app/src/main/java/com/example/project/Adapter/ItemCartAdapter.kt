package com.example.project.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.Interface.ActiveCount
import com.example.project.Model.Entity.Cart
import com.example.project.R
import com.example.project.databinding.RowItemCartBinding

class ItemCartAdapter(
    private val list: List<Cart>,
    private val onClick: ActiveCount
) : RecyclerView.Adapter<ItemCartAdapter.RV>() {

    inner class RV(private val binding: RowItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Cart, position: Int) {
            Glide.with(binding.root.context).load(item.img).into(binding.imgProduct)
            binding.tvName.text = item.name
            binding.tvPrice.text = item.price
            binding.tvQuantity.text = item.quantity.toString()
            binding.tvSize.text = item.size
            binding.tvColor.text = item.color
            val pricePerUnit = item.price.replace("k", "", ignoreCase = true).trim().toIntOrNull() ?: 0
            binding.tvPriceToatal.text = "${pricePerUnit * item.quantity}k"

            binding.btnAdd.setOnClickListener {
                onClick.add(position)
            }

            binding.btnSub.setOnClickListener {
                onClick.sub(position)
            }

            binding.btnDelete.setOnClickListener {
                onClick.delete(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RV, position: Int) {
        holder.bind(list[position], position)
    }
}
