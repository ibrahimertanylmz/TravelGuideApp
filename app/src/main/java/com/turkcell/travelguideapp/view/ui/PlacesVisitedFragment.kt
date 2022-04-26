package com.turkcell.travelguideapp.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var dbOperation: TravelGuideOperation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlacesVisitedBinding.inflate(inflater)

        (requireActivity() as MainActivity).binding.includeTop.tvTopBarTitle.text =
            getString(R.string.str_visited_places)
        (requireActivity() as MainActivity).changeBackButtonVisibility(false)
        (requireActivity() as MainActivity).changeTabLayoutVisibility(true)
        (requireActivity() as MainActivity).changeViewPagerVisibility(true)

        binding.rvPlaceVisited.layoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        dbOperation = TravelGuideOperation(requireContext())

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.includeTop.tvTopBarTitle.text =
            getString(R.string.visited_places)
        list = PlaceLogic.returnVisitedPlaces(dbOperation)
        binding.rvPlaceVisited.adapter = PlaceAdapter(requireContext(), list, ::itemClick)
        binding.rvPlaceVisited.adapter!!.notifyDataSetChanged()
    }

    private fun itemClick(position: Int) {
        (activity as MainActivity).binding.viewpager.visibility = View.INVISIBLE
        (activity as MainActivity).binding.fragmentContainer.visibility = View.VISIBLE

        val action =
            PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToPlaceDetailsFragment(
                list[position].id
            )
        findNavController().navigate(action)
    }

}