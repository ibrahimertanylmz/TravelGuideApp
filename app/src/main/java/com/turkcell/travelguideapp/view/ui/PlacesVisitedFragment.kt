package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.databinding.FragmentPlacesVisitedBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.view.adapter.PlaceAdapter


class PlacesVisitedFragment : Fragment() {

    private lateinit var binding: FragmentPlacesVisitedBinding
    private lateinit var list: ArrayList<Place>
    lateinit var dbOperation: TravelGuideOperation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlacesVisitedBinding.inflate(inflater)

        binding.rvPlaceVisited.layoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        dbOperation = TravelGuideOperation(requireContext())

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        list = PlaceLogic.returnVisitedPlaces(dbOperation)
        binding.rvPlaceVisited.adapter = PlaceAdapter(requireContext(), list, ::itemClick)
    }

    fun itemClick(position: Int) {
        val action =
            PlacesVisitedFragmentDirections.actionPlacesVisitedFragmentToPlaceDetailsFragment(
                position
            )
        findNavController().navigate(action)
    }

}