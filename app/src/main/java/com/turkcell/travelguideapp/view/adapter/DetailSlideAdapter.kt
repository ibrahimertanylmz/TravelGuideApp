package com.turkcell.travelguideapp.view.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.view.viewholder.DetailSlideViewHolder

class DetailSlideAdapter(var imageList:ArrayList<Bitmap>) : RecyclerView.Adapter<DetailSlideViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailSlideViewHolder {
        return DetailSlideViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_pager_item_page,parent,false))
    }

    override fun onBindViewHolder(holder:DetailSlideViewHolder, position: Int) {
        holder.imageView.setImageBitmap(imageList.get(position))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}