package com.example.projectmanage.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanage.databinding.RowItemImgBinding

class ListImgProduct1(private val ds: MutableList<String>?) : RecyclerView.Adapter<ListImgProduct1.RV>() {

    class RV(private val binding: RowItemImgBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.img
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun getItemCount() = ds?.size ?: 0

    override fun onBindViewHolder(holder: RV, position: Int) {
        Glide.with(holder.itemView.context).load(ds?.get(position)).into(holder.img)
    }
}
