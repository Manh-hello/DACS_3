package com.example.project.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.databinding.RowEvaluateBinding
import com.example.project.databinding.RowOrderBinding
import com.example.projectmanage.Model.Entity.Evaluate

class EvaluateAdapter(private val ds: MutableList<Evaluate>): RecyclerView.Adapter<EvaluateAdapter.RV>() {
    class RV(private val binding: RowEvaluateBinding): RecyclerView.ViewHolder(binding.root){
        fun init(item: Evaluate){
            binding.desc.setText(item.desc)
            if (item.img.isNotEmpty()){
                Glide.with(binding.img).load(item.img).into(binding.img)
                binding.img.visibility = View.VISIBLE
            }
            binding.ratingBar.rating = item.rating.toFloat()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RV {
        val binding = RowEvaluateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RV(binding)
    }

    override fun onBindViewHolder(
        holder: RV,
        position: Int
    ) {
        holder.init(ds[position])
    }

    override fun getItemCount()=ds.size
}