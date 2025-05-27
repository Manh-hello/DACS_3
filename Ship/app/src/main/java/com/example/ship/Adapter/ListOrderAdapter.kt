package com.example.ship.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ship.Interface.OnClickOrder
import com.example.ship.Model.OrderDetail
import com.example.ship.R
import com.example.ship.databinding.RowItemOrderBinding

class ListOrderAdapter(
    val list: MutableList<OrderDetail>,
    private val onClick: OnClickOrder
) : RecyclerView.Adapter<ListOrderAdapter.RV>() {

    inner class RV(private val binding: RowItemOrderBinding): RecyclerView.ViewHolder(binding.root){
        fun init(item: OrderDetail,i:Int){
            binding.apply {
                tvName.setText(item.name)
                tvStatus.setText(item.status)
                Glide.with(binding.img).load(item.src).into(binding.img)
            }
            if(item.status == "Đã xác nhận"){
                binding.btnAccess.setText("Nhận")
            }else{
                binding.btnAccess.setText("Đã giao")
            }
            if (item.status == "Đã giao" || item.status == "Đã nhận"){
                binding.btnAccess.visibility = View.GONE
            }
            binding.apply {
                if(item.status == "Đã xác nhận"){
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey))
                    ic.setImageResource(R.drawable.ic_tick_mono)
                }else if (item.status == "Đang giao"){
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.blue))
                    ic.setImageResource(R.drawable.ic_car)
                }else if (item.status == "Đã nhận"){
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.green))
                    ic.setImageResource(R.drawable.ic_tick)
                }else{
                    tvStatus.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey))
                    ic.setImageResource(R.drawable.ic_tick_mono)
                }
            }

            binding.btnInfo.setOnClickListener {
                onClick.info(i)
            }
            binding.btnAccess.setOnClickListener {
                onClick.select(i)
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
        holder.init(list[position],position)
    }

    override fun getItemCount()=list.size
}