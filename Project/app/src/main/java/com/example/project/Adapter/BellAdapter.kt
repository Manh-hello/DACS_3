package com.example.project.Adapter

import android.R
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.Activity.CartActivity
import com.example.project.Activity.OrderActivity
import com.example.project.Model.Entity.Notification
import com.example.project.Repository.Notifications
import com.example.project.databinding.RowItemBellBinding

class BellAdapter(val ds:MutableList<Notification>):RecyclerView.Adapter<BellAdapter.RV>() {
    class RV(private val binding:RowItemBellBinding):RecyclerView.ViewHolder(binding.root){
        fun init(item: Notification){
            binding.title.text = item.title
            binding.descript.text = item.mes
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val view = RowItemBellBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RV(view)
    }

    override fun getItemCount()=ds.size

    override fun onBindViewHolder(holder: RV, position: Int) {
        val item = ds[position]
        holder.init(item)
        holder.itemView.setOnClickListener {
            if (item.type == "Đặt Hàng"){
                holder.itemView.context.startActivity(Intent(holder.itemView.context, OrderActivity::class.java))
            }else if(item.type == "Add Cart"){
                holder.itemView.context.startActivity(Intent(holder.itemView.context, CartActivity::class.java))
            }else if (item.type == "Hủy"){
                val intent = Intent(holder.itemView.context, OrderActivity::class.java)
                intent.putExtra("selected_tab", 4) // ví dụ Tab 2 là Notification
                holder.itemView.context.startActivity(intent)
            }
        }
    }

}