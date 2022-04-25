package com.turkcell.travelguideapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.view.viewholder.PlaceViewHolder

class PlaceAdapter(
    var context: Context,
    var liste: ArrayList<Place>,
    var itemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<PlaceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.rv_places_to_visit_item, parent, false)
        return PlaceViewHolder(v, itemClick)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bindData(liste.get(position))
    }

    override fun getItemCount(): Int {
        return liste.size
    }
}