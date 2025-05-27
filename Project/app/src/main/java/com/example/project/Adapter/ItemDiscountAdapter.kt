package com.example.project.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.Interface.SelectDiscount
import com.example.project.Model.Entity.MyDiscount
import com.example.project.databinding.RowDiscountBinding
import com.example.projectmanage.Model.Entity.Discount

class ItemDiscountAdapter(
    var ds: MutableList<Discount>,
    var ds2: MutableList<MyDiscount>,
    private val onClick: SelectDiscount
) : RecyclerView.Adapter<ItemDiscountAdapter.RV>() {

    inner class RV(private val binding: RowDiscountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Discount, i: Int) {
            binding.tvReduce.text = item.reduce

            val isTaken = ds2.any { it.discountId == item.id }
            if (isTaken) {
                binding.btnSelect.text = "Đã lấy"
                binding.btnSelect.isEnabled = false
                binding.btnSelect.alpha = 0.5f
            } else {
                binding.btnSelect.text = "Lấy"
                binding.btnSelect.isEnabled = true
                binding.btnSelect.alpha = 1f

                binding.btnSelect.setOnClickListener {
                    onClick.onClick(i)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowDiscountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun onBindViewHolder(holder: RV, position: Int) {
        holder.bind(ds[position], position)
    }

    override fun getItemCount(): Int = ds.size
}
