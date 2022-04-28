package com.turkcell.travelguideapp.view.viewholder

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.R

class DetailSlideViewHolder(itemview: View, var itemClick:(position:Int)->Unit) : RecyclerView.ViewHolder(itemview){
    var imageView:ImageView

    init {
        imageView=itemview.findViewById(R.id.ivGalleryImage)
        itemview.setOnClickListener {
            itemClick(adapterPosition)
        }
    }


    fun bind(bitmap:Bitmap){
        imageView.setImageBitmap(bitmap)
    }
}