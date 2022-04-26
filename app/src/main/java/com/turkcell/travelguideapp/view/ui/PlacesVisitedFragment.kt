package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
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
        (activity as MainActivity).binding.includeTop.tvTopBarTitle.text =
            getString(R.string.visited_places)
        list = PlaceLogic.returnVisitedPlaces(dbOperation)
        binding.rvPlaceVisited.adapter = PlaceAdapter(requireContext(), list, ::itemClick)
        binding.rvPlaceVisited.adapter!!.notifyDataSetChanged()
    }

    private fun itemClick(position: Int) {
        val action =
            PlacesVisitedFragmentDirections.actionPlacesVisitedFragmentToPlaceDetailsFragment(
                position
            )
        findNavController().navigate(action)
    }

}