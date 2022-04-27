package com.turkcell.travelguideapp.view.viewholder

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority

class PlaceViewHolder(itemView: View, var itemClick: (position: Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    var placeImage: ImageView
    var tvPlaceName: TextView
    var tvPlaceDefinition: TextView
    var tvPlaceDescription: TextView
    var priorityImage: ImageView
    var tvLastVisitDate: TextView

    init {
        placeImage = itemView.findViewById(R.id.ivPlaceImage)
        tvPlaceName = itemView.findViewById(R.id.tvPlaceName)
        tvPlaceDefinition = itemView.findViewById(R.id.tvNameDescription)
        tvPlaceDescription = itemView.findViewById(R.id.tvShortExplanation)
        priorityImage = itemView.findViewById(R.id.ivOval)
        tvLastVisitDate = itemView.findViewById(R.id.tvDate)

        itemView.setOnClickListener {
            itemClick(adapterPosition)
        }

    }

    fun bindData(place: Place) {
        if (place.imageList.size > 0) {
            placeImage.setImageBitmap(place.imageList[0])
        }else{
            placeImage.setImageResource(R.drawable.image_placeholder)
        }
        //düzeltmeyi UNUTMA
        tvPlaceName.text = place.id.toString()
        //tvPlaceName.text = place.name
        tvPlaceDefinition.text = place.definition
        tvPlaceDescription.text = place.description

        if (place.lastVisitDate != null) {
            tvLastVisitDate.visibility = View.VISIBLE
            tvLastVisitDate.text = place.lastVisitDate
            priorityImage.visibility = View.GONE
        } else {
            when (place.priority) {
                Priority.ONE -> priorityImage.setImageResource(R.drawable.rv_oval_item_green)
                Priority.TWO -> priorityImage.setImageResource(R.drawable.rv_oval_item_blue)
                Priority.THREE -> priorityImage.setImageResource(R.drawable.rv_oval_item_gray)
            }
        }
    }
}