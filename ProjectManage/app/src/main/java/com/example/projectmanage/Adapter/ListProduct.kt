package com.example.projectmanage.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanage.Activity.FixProduct
import com.example.projectmanage.Model.ProductFull
import com.example.projectmanage.databinding.RowListProductBinding

class ListProduct( private val ds: List<ProductFull>,
                   private val onItemClick: (ProductFull) -> Unit
) : RecyclerView.Adapter<ListProduct.RV>() {

    inner class RV(val binding: RowListProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun getItemCount() = ds.size

    override fun onBindViewHolder(holder: RV, position: Int) {
        val item = ds[position]

        if (item.imgs.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.imgs[0])
                .into(holder.binding.img)
        }

        holder.binding.tvName.text = item.product.name
        holder.binding.tvQuantity.text = "SL: ${item.product.quantity}"

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
