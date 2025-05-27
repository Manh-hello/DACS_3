package com.example.project.Adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.example.project.databinding.RowImgProductBinding

class SlideProductAdapter(private var ds: MutableList<String>, private val viewPager2: ViewPager2):
    RecyclerView.Adapter<SlideProductAdapter.slide>() {
    private lateinit var context: Context
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = Runnable {
        viewPager2.currentItem = (viewPager2.currentItem ) % ds.size
    }

    class slide(private val binding: RowImgProductBinding): RecyclerView.ViewHolder(binding.root){
        fun setImage(url: String, context: Context) {
            Glide.with(context)
                .load(url)
                .apply(RequestOptions().transform(CenterInside()))
                .into(binding.imageSlide)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): slide {
        context = parent.context
        val binding = RowImgProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return slide(binding)
    }

    override fun onBindViewHolder(
        holder: slide,
        position: Int
    ) {
        holder.setImage(ds[position],context)
        if (position == ds.lastIndex-1){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount()=ds.size
}