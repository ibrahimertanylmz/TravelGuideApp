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
import com.turkcell.travelguideapp.bll.ImageLogic
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.databinding.FragmentPlacesToVisitBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.view.adapter.PlaceAdapter

class PlacesToVisitFragment : Fragment() {

    private lateinit var binding: FragmentPlacesToVisitBinding
    private lateinit var list: ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlacesToVisitBinding.inflate(inflater)

        binding.rvPlaceToVisit.layoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        initializeViews()
        initializeEvents()

        return binding.root
    }

    private fun initializeViews() {
        (requireActivity() as MainActivity).changeMainActivityHuds(
            setBackButtonVisible = false,
            setTabLayoutVisibleAndBtnWideInvisible = true,
            setViewPagerVisible = true,
            titleString = getString(R.string.str_places_to_visit)
        )
        list = PlaceLogic.returnPlacesToVisit(dbOperation)
        list.forEach {
            it.imageList = ImageLogic.getImagesByPlaceId(requireContext(),it.id)
        }
        binding.rvPlaceToVisit.adapter = PlaceAdapter(requireContext(), list, ::itemClick)
        binding.rvPlaceToVisit.adapter!!.notifyDataSetChanged()
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