package com.example.project.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.Model.Pic
import com.example.project.R
import com.example.project.databinding.RowItemListPicBinding

class ListPicAdapter(val ds: MutableList<String>) : RecyclerView.Adapter<ListPicAdapter.RV>() {

    private var selectedPosition = 0

    private lateinit var listener: onItemClick

    interface onItemClick {
        fun onClick(i: Int)
    }

    fun setOnItemClickListener(l: onItemClick) {
        listener = l
    }


    class RV(val binding: RowItemListPicBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.img
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding =
            RowItemListPicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun getItemCount() = ds.size

    override fun onBindViewHolder(holder: RV, position: Int) {
        val item = ds[position]
        Glide.with(holder.itemView.context)
            .load(item)
            .into(holder.img)
        if (position == selectedPosition) {
            holder.binding.root.setBackgroundResource(R.drawable.selected_pic_bg)
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.default_pic_bg)
        }
        holder.binding.root.setOnClickListener {
            val previous = selectedPosition
            selectedPosition = holder.adapterPosition

            notifyItemChanged(previous)
            notifyItemChanged(selectedPosition)

            listener.onClick(selectedPosition)
        }
    }
    fun updateSelectedPosition(position: Int) {
        val previous = selectedPosition
        selectedPosition = position
        notifyItemChanged(previous)
        notifyItemChanged(selectedPosition)
    }
}
