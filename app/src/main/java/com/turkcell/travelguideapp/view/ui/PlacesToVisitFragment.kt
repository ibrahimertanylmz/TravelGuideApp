package com.turkcell.travelguideapp.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.databinding.FragmentPlacesToVisitBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.view.adapter.PlaceAdapter

class PlacesToVisitFragment : Fragment() {

    private lateinit var binding: FragmentPlacesToVisitBinding
    private lateinit var list: ArrayList<Place>
    private lateinit var dbOperation: TravelGuideOperation



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlacesToVisitBinding.inflate(inflater)

        (requireActivity() as MainActivity).binding.includeTop.tvTopBarTitle.text =
            getString(R.string.str_places_to_visit)
        (requireActivity() as MainActivity).changeBackButtonVisibility(false)
        (requireActivity() as MainActivity).changeTabLayoutVisibility(true)
        (requireActivity() as MainActivity).changeViewPagerVisibility(true)

        binding.rvPlaceToVisit.layoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        dbOperation = TravelGuideOperation(requireContext())

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.includeTop.tvTopBarTitle.text =
            getString(R.string.places_to_visit)
        list = PlaceLogic.returnPlacesToVisit(dbOperation)
        binding.rvPlaceToVisit.adapter = PlaceAdapter(requireContext(), list, ::itemClick)
        binding.rvPlaceToVisit.adapter!!.notifyDataSetChanged()
    }

    private fun itemClick(position: Int) {
        (activity as MainActivity).binding.viewpager.visibility = View.INVISIBLE
        (activity as MainActivity).binding.fragmentContainer.visibility = View.VISIBLE

        val action =
            PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToPlaceDetailsFragment(
                list[position].id
            )

        findNavController().navigate(action)

        Toast.makeText(context, action.arguments.toString(), Toast.LENGTH_SHORT).show()
        //(requireActivity()).findNavController(R.id.fragment).navigate(R.id.action_placesToVisitFragment_to_placeDetailsFragment)
    }

}