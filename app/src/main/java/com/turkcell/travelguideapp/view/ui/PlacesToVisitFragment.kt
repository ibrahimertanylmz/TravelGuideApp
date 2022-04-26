package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.databinding.FragmentPlacesToVisitBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.view.adapter.PlaceAdapter

class PlacesToVisitFragment : Fragment() {

    private lateinit var binding: FragmentPlacesToVisitBinding
    private lateinit var list: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlacesToVisitBinding.inflate(inflater)

        binding.rvPlaceToVisit.layoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        list = PlaceLogic.returnPlacesToVisit()
        binding.rvPlaceToVisit.adapter =
            PlaceAdapter(requireContext(), list, ::itemClick)
    }

    private fun itemClick(position: Int) {
        val action =
            PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToPlaceDetailsFragment(
                position
            )
        findNavController().navigate(action)
    }

}