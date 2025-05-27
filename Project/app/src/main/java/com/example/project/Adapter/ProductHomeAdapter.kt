package com.example.project.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.Model.ProductFull
import com.example.project.R

class ProductHomeAdapter(var ds: MutableList<ProductFull>) : RecyclerView.Adapter<ProductHomeAdapter.RV>() {

    private lateinit var listener: onItemClick

    interface onItemClick {
        fun onClick(i: Int)
    }

    fun setOnItemClickListener(l: onItemClick) {
        listener = l
    }

    class RV(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img = itemView.findViewById<ImageView>(R.id.img1)
        var name = itemView.findViewById<TextView>(R.id.name)
        var price = itemView.findViewById<TextView>(R.id.price)
        var star = itemView.findViewById<TextView>(R.id.star)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false)
        return RV(view)
    }

    override fun getItemCount() = ds.size

    override fun onBindViewHolder(holder: RV, position: Int) {
        val item = ds[position]
        Glide.with(holder.itemView.context)
            .load(item.imgs[0])
            .into(holder.img)
        holder.name.text = item.product.name
        holder.price.text = item.product.price
        holder.star.text = item.product.star.toString()

        holder.itemView.setOnClickListener {
            listener.onClick(position)
        }
    }
}
