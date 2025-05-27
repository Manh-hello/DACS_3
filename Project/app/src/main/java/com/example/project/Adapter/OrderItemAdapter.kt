package com.example.project.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.Interface.ClickBTNOrder
import com.example.project.databinding.RowOrderItemBinding
import com.example.projectmanage.Model.Entity.OrderDetail

class OrderItemAdapter(private val ds: MutableList<OrderDetail>,
                       private val onClick: ClickBTNOrder
) : RecyclerView.Adapter<OrderItemAdapter.RV>(){
    inner class RV(private val binding: RowOrderItemBinding): RecyclerView.ViewHolder(binding.root){
        fun init(item: OrderDetail,i:Int){
            binding.apply {
                Glide.with(img).load(item.src).into(img)
                tvName.text = item.name
                tvColor.text = item.color
                tvSize.text = item.size
                tvPrice.text = item.price
                date.text = "Ngày đặt hàng: ${item.date}"
                tvQuantity.text = "x${item.quantity}"
            }

            if(item.status == "Chờ cửa hàng xác nhận"){
                binding.btnAccess.visibility = View.GONE
            }else if(item.status == "Đã xác nhận" || item.status == "Đang giao" || item.status == "Đã hủy"){
                binding.btnError.visibility = View.GONE
                binding.btnAccess.visibility = View.GONE
            }else if(item.status == "Đã nhận"){
                binding.btnError.visibility = View.GONE
                binding.btnAccess.visibility = View.GONE
                if (item.evaluate == true){
                    binding.btnEvaluate.visibility = View.GONE
                }else{
                    binding.btnEvaluate.visibility = View.VISIBLE
                }
            }else if(item.status == "Đã giao"){
                binding.btnAccess.setText("Đã nhận")
                binding.btnAccess.visibility = View.VISIBLE
                binding.btnError.visibility = View.GONE
            }


            binding.btnError.setOnClickListener {
                onClick.clickError(item.id)
            }
            binding.btnAccess.setOnClickListener {
                onClick.clickSuccess(item.id)
            }
            binding.btnInfo.setOnClickListener {
                onClick.clickInfo(item)
            }
            binding.btnEvaluate.setOnClickListener {
                onClick.clickEvaluate(item.id)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RV {
        val binding = RowOrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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

