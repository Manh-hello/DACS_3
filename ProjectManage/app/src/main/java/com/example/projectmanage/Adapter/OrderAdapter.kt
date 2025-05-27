package com.example.projectmanage.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanage.Interface.ClickBTNOrder
import com.example.projectmanage.Model.Entity.OrderDetail
import com.example.projectmanage.R
import com.example.projectmanage.databinding.RowItemOrderBinding

class OrderAdapter(private val ds: MutableList<OrderDetail>, val onClick: ClickBTNOrder) : RecyclerView.Adapter<OrderAdapter.RV>(){

    inner class RV(private val binding: RowItemOrderBinding): RecyclerView.ViewHolder(binding.root){
        fun init(item: OrderDetail,i:Int){
            binding.apply {
                Glide.with(img).load(item.src).into(img)
                tvName.text = item.name
                tvStatus.text = item.status

                if (item.status == "Chờ cửa hàng xác nhận"){
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.orange))
                    ic.setImageResource(R.drawable.ic_clock)
                }else if(item.status == "Đã xác nhận"){
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey))
                    ic.setImageResource(R.drawable.ic_tick_mono)
                }else if(item.status == "Đã hủy"){
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.red))
                    ic.setImageResource(R.drawable.ic_x)
                }else if(item.status == "Đang giao"){
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.blue))
                    ic.setImageResource(R.drawable.ic_car)
                }else if(item.status == "Đã nhận"){
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.green))
                    ic.setImageResource(R.drawable.ic_tick)
                }else{

                }

                if (item.status != "Chờ cửa hàng xác nhận"){
                    binding.btnAccess.alpha = 0.5f
                    binding.btnAccess.isEnabled = false
                    binding.btnError.alpha = 0.5f
                    binding.btnError.isEnabled = false
                }else{
                    binding.btnAccess.alpha = 1f
                    binding.btnError.alpha = 1f
                    binding.btnError.isEnabled = true
                    binding.btnAccess.isEnabled = true
                }

                binding.btnError.setOnClickListener {
                    onClick.clickError(i)
                }
                binding.btnAccess.setOnClickListener {
                    onClick.clickSuccess(i)
                }
                binding.btnInfo.setOnClickListener {
                    onClick.clickInfo(i)
                }
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RV {
        val binding = RowItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RV(binding)
    }

    override fun onBindViewHolder(
        holder: RV,
        position: Int
    ) {
        holder.init(ds[position],position)
    }

    override fun getItemCount()=ds.size

}