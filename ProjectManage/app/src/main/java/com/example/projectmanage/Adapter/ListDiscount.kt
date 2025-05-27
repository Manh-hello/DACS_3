package com.example.projectmanage.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanage.Model.Entity.Discount
import com.example.projectmanage.Model.ProductFull
import com.example.projectmanage.databinding.RowListDiscountBinding

class ListDiscount(
    var ds: List<Discount>,
    private val onItemClick: (Discount) -> Unit
): RecyclerView.Adapter<ListDiscount.RV>() {
    class RV(val binding: RowListDiscountBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RV {
        val binding = RowListDiscountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun onBindViewHolder(
        holder: RV,
        i: Int
    ) {
        holder.apply {
            binding.tvName.setText(ds[i].code)
            binding.tvReduce.setText("Giảm: ${ds[i].reduce}")
            binding.tvQuantity.setText(ds[i].count.toString())
        }
        holder.itemView.setOnClickListener {
            onItemClick(ds[i])
        }
    }

    override fun getItemCount()=ds.size


}