package com.turkcell.travelguideapp.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.model.Visitation

class PhotoViewHolder(
    itemView: View,
    var itemClick: (position: Int) -> Unit,
    var itemButtonClick: (position: Int) -> Unit,
    var itemAddPhotoClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    var imageAddPhoto: ImageView
    var iconDelete: ImageView

    init {

        imageAddPhoto = itemView.findViewById(R.id.imageAddPhoto)
        iconDelete = itemView.findViewById(R.id.iconDelete)

        itemView.setOnClickListener {
            itemClick(adapterPosition)
        }

        iconDelete.setOnClickListener {
            itemButtonClick(adapterPosition)
        }

        imageAddPhoto.setOnClickListener {
            itemAddPhotoClick(adapterPosition)
        }

    }

    fun bind(any: Any) {
        // imagePhoto=visitation.imageList.get()

    }


}