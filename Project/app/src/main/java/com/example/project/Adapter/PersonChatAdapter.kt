package com.example.project.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.Interface.ClickCUS
import com.example.project.Model.Entity.Admin
import com.example.project.databinding.RowPersonChatBinding

class PersonChatAdapter(var list:MutableList<Admin>, private val onClic: ClickCUS): RecyclerView.Adapter<PersonChatAdapter.RV>() {
    class RV(private val binding: RowPersonChatBinding): RecyclerView.ViewHolder(binding.root){
        fun init(item: Admin){
            if (item.img.isNotEmpty()){
                Glide.with(binding.avatar).load(item.img).into(binding.avatar)
            }
            binding.namePerson.setText(item.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RV {
        val binding = RowPersonChatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RV(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RV, position: Int) {
        holder.init(list[position])
        holder.itemView.setOnClickListener{
            onClic.click(position)
        }
    }
}