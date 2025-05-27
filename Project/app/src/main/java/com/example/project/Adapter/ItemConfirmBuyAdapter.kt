package com.example.project.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.Model.Item_ConfirmBuy
import com.example.project.Model.ProductBuy
import com.example.project.databinding.RowItemConfirmbuyBinding

class ItemConfirmBuyAdapter(var list: MutableList<ProductBuy>):RecyclerView.Adapter<ItemConfirmBuyAdapter.RV>() {
    class RV(private var binding:RowItemConfirmbuyBinding):RecyclerView.ViewHolder(binding.root){
        fun init(item: ProductBuy){
            Glide.with(binding.root.context).load(item.img).into(binding.imgProduct)
            binding.tvName.text = item.name
            binding.tvSize.text = item.size
            binding.tvColor.text = item.color
            binding.tvPrice.text = item.price
            binding.tvQuantity.text = "x${item.quantity.toString()}"

            val number = item.price.replace("k","", ignoreCase = true).trim().toIntOrNull()?:0
            binding.tvPriceToatal.setText("${item.quantity*number}k")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowItemConfirmbuyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RV(binding)
    }

    override fun getItemCount()=list.size

    override fun onBindViewHolder(holder: RV, i: Int) {
        holder.init(list[i])
    }
}