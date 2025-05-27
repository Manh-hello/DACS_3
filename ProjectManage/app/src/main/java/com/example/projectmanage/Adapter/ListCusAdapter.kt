package com.example.projectmanage.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanage.Interface.ClickCUS
import com.example.projectmanage.Model.Entity.Customer
import com.example.projectmanage.databinding.RowCusBinding

class ListCusAdapter(private val ds: MutableList<Customer>, private val onClick: ClickCUS) :
    RecyclerView.Adapter<ListCusAdapter.RV>() {

    inner class RV(private val binding: RowCusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun init(item: Customer) {
            if (item.srcimg.isNotEmpty()) {
                Glide.with(binding.avatar).load(item.srcimg).into(binding.avatar)
            }
            binding.namePerson.setText(item.name)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RV {
        val binding = RowCusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun onBindViewHolder(
        holder: RV,
        position: Int
    ) {
        holder.init(ds[position])
        holder.itemView.setOnClickListener {
            onClick.click(position)
        }
    }

    override fun getItemCount() = ds.size
}