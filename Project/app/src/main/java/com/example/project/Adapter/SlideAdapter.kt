package com.example.project.Adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.example.project.Model.SlideModel
import com.example.project.databinding.RowSliderItemContainnerBinding
class SlideAdapter(private var slideItems: List<SlideModel>, private val viewPager2: ViewPager2) : RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {

    private lateinit var context: Context
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var listener: onItemClick

    interface onItemClick {
        fun onClick(text: String)
    }

    fun setOnItemClickListener(l: onItemClick) {
        listener = l
    }

    private val runnable = Runnable {
        viewPager2.currentItem = (viewPager2.currentItem ) % slideItems.size
    }
    class SlideViewHolder(private val binding: RowSliderItemContainnerBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv = binding.tvCategory
        fun setImage(sliderItem: SlideModel, context: Context) {
            Glide.with(context)
                .load(sliderItem.imageRes)
                .apply(RequestOptions().transform(CenterInside()))
                .into(binding.imageSlide)
            tv.text = sliderItem.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        context = parent.context
        val binding = RowSliderItemContainnerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SlideViewHolder(binding)
    }

    override fun getItemCount(): Int = slideItems.size

    override fun onBindViewHolder(holder: SlideViewHolder, i: Int) {
        holder.setImage(slideItems[i], context)
        if(i == slideItems.lastIndex-1){
            viewPager2.post(runnable)
        }
        holder.itemView.setOnClickListener {
            listener.onClick(slideItems[i].category)
        }
        Log.d("SlideAdapter", "Loading image: ${slideItems[i].imageRes}, position: $i")
    }
}
