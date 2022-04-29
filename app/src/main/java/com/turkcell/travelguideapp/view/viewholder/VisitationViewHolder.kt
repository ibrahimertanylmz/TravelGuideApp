package com.turkcell.travelguideapp.view.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.travelguideapp.databinding.RvVisitHistoryItemBinding
import com.turkcell.travelguideapp.model.Visitation

class VisitationViewHolder(
    private val binding: RvVisitHistoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(visitation: Visitation) {
        binding.tvVisitHistoryDate.text = visitation.date
        binding.tvVisitHistoryDescription.text = visitation.description
    }
}