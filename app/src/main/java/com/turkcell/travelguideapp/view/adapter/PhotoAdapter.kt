package com.turkcell.travelguideapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.view.viewholder.PhotoViewHolder
import com.turkcell.travelguideapp.R

class PhotoAdapter(
    val context: Context,
    var imageList: ArrayList<Any>,
    var itemClick: (position: Int) -> Unit,
    var itemButtonClick: (position: Int) -> Unit,
    var itemAddPhotoClick: (position: Int) -> Unit
) : RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(v, itemClick, itemButtonClick, itemAddPhotoClick)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}