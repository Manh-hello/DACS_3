package com.example.project.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.Model.ProductFull
import com.example.project.databinding.RowListProductFind1Binding

class FindAdapter(private var list: MutableList<ProductFull>, ) : RecyclerView.Adapter<FindAdapter.RV>(){
    private lateinit var listener: onItemClick

    interface onItemClick {
        fun onClick(i: Int)
    }

    fun setOnItemClickListener(l: onItemClick) {
        listener = l
    }

    class RV(private val binding: RowListProductFind1Binding): RecyclerView.ViewHolder(binding.root){
        fun init(item: ProductFull){
            binding.apply {
                Glide.with(img).load(item.imgs[0]).into(img)
                tvName.text = item.product.name
                tvPrice.text = item.product.price
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RV {
        val binding = RowListProductFind1Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RV(binding)
    }

    override fun onBindViewHolder(
        holder: RV,
        position: Int
    ) {
        holder.init(list[position])
        holder.itemView.setOnClickListener {
            listener.onClick(position)
        }
    }

    override fun getItemCount()= list.size
}