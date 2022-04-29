package com.turkcell.travelguideapp.view.viewholder

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.R

class PhotoViewHolder(
    itemView: View,
    var itemClick: (position: Int) -> Unit,
    var itemButtonClick: (position: Int) -> Unit,
    var itemAddPhotoClick: (position: Int) -> Unit,
    var imageList: ArrayList<Bitmap>
) : RecyclerView.ViewHolder(itemView) {

    var imagePhoto: ImageView
    var iconDelete: ImageView

    init {
        imagePhoto = itemView.findViewById(R.id.imagePhoto)
        iconDelete = itemView.findViewById(R.id.iconDelete)

        itemView.setOnClickListener {
            itemClick(adapterPosition)
        }

        iconDelete.setOnClickListener {
            itemButtonClick(adapterPosition)
        }

        /*imageAddPhoto.setOnClickListener {
            itemAddPhotoClick(adapterPosition)
            addPhotoLayout.visibility = View.GONE
        }*/

    }

    fun bind(bitmap: Bitmap) {
        imagePhoto.setImageBitmap(bitmap)
    }

}