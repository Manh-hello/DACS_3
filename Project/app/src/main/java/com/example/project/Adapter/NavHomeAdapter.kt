package com.example.project.Adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.Interface.SelectNav
import com.example.project.Model.Nav_Home
import com.example.project.databinding.RowNavBinding

class NavHomeAdapter(
    var ds: MutableList<Nav_Home>,
    val onClick: SelectNav
) : RecyclerView.Adapter<NavHomeAdapter.RV>() {

    inner class RV(val binding: RowNavBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Nav_Home) {
            binding.img.setImageResource(item.img)
            binding.txt.text = item.title

            binding.root.setOnClickListener {
                onClick.select(item.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowNavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RV(binding)
    }

    override fun getItemCount() = ds.size

    override fun onBindViewHolder(holder: RV, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        layoutParams.width = (Resources.getSystem().displayMetrics.widthPixels / 4)
        holder.itemView.layoutParams = layoutParams

        holder.bind(ds[position])
    }
}
