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
import com.turkcell.travelguideapp.databinding.FragmentPlacesToVisitBinding
import com.turkcell.travelguideapp.view.adapter.PlaceAdapter

class PlacesToVisitFragment : Fragment() {

    private lateinit var binding: FragmentPlacesToVisitBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlacesToVisitBinding.inflate(inflater)

        binding.rvPlaceToVisit.layoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        //initializeViews()
        initializeEvents()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        initializeViews()
    }

    private fun initializeViews() {
        (requireActivity() as MainActivity).changeMainActivityUI(
            setBackButtonVisible = false,
            titleString = getString(R.string.str_places_to_visit),
            setViewPagerVisible = true,
            setTabLayoutVisible = true,
            setTabLayoutClickable = true,
            setBtnAddPlaceVisible = true,
            setBtnWideVisible = false
        )

        binding.rvPlaceToVisit.adapter = PlaceAdapter(requireContext(), PlaceLogic.returnPlacesToVisit(dbOperation), ::itemClick)
    }

    private fun initializeEvents() {
        (requireActivity() as MainActivity).binding.includeBottom.btnAddPlace.setOnClickListener {
            val action =
                PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToAddPlaceFragment()
            findNavController().navigate(action)
        }
    }

    private fun itemClick(position: Int) {
        val action =
            PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToPlaceDetailsFragment(
                PlaceLogic.returnPlacesToVisit(dbOperation)[position].id
            )
        findNavController().navigate(action)
    }

}