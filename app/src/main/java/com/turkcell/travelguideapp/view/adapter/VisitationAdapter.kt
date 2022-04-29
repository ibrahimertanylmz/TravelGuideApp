package com.turkcell.travelguideapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.databinding.RvVisitHistoryItemBinding
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.viewholder.VisitationViewHolder

class VisitationAdapter(
    val context: Context,
    private var list: ArrayList<Visitation>
) : RecyclerView.Adapter<VisitationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitationViewHolder {
        val from = LayoutInflater.from(context)
        val binding = RvVisitHistoryItemBinding.inflate(from, parent, false)
        return VisitationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VisitationViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size
}