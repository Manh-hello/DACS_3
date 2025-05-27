package com.example.projectmanage.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanage.databinding.RowItemImgBinding

class ListImgProduct(private val ds: List<Uri>) : RecyclerView.Adapter<ListImgProduct.RV>() {

    class RV(private val binding: RowItemImgBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.img
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun getItemCount() = ds.size

    override fun onBindViewHolder(holder: RV, position: Int) {
        holder.img.setImageURI(ds[position])
    }
}
