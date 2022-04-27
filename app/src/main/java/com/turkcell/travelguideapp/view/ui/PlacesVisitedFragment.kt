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
import com.turkcell.travelguideapp.databinding.FragmentPlacesVisitedBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.view.adapter.PlaceAdapter

class PlacesVisitedFragment : Fragment() {

    private lateinit var binding: FragmentPlacesVisitedBinding
    private lateinit var list: ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlacesVisitedBinding.inflate(inflater)

        binding.rvPlaceVisited.layoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        initializeViews()
        initializeEvents()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeViews() {
        (requireActivity() as MainActivity).changeMainActivityHuds(
            setBackButtonVisible = false,
            setTabLayoutVisibleAndBtnWideInvisible = true,
            setViewPagerVisible = true,
            titleString = getString(R.string.str_visited_places)
        )
        list = PlaceLogic.returnVisitedPlaces(dbOperation)
        binding.rvPlaceVisited.adapter = PlaceAdapter(requireContext(), list, ::itemClick)
        binding.rvPlaceVisited.adapter!!.notifyDataSetChanged()
    }

    private fun initializeEvents() {
        (requireActivity() as MainActivity).binding.includeBottom.btnAddPlace.setOnClickListener {
            val action =
                PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToAddPlaceFragment()
            findNavController().navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        initializeViews()
    }

    private fun itemClick(position: Int) {
        val action =
            PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToPlaceDetailsFragment(
                list[position].id
            )
        findNavController().navigate(action)
    }

}