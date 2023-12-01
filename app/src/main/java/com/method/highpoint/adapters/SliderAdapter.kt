package com.method.highpoint.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.method.highpoint.R

class SliderAdapter(private val images: List<Int>): RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slides_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentImage = images[position]
        holder.imageView.setImageResource(currentImage)
        val param = holder.imageView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 56, 0,0)
        holder.imageView.layoutParams = param
        holder.imageView.scaleX = 1.2F
        holder.imageView.scaleY = 1.05F
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.slider_image)
    }

}