package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.bll.VisitationLogic
import com.turkcell.travelguideapp.databinding.FragmentPlaceDetailsBinding
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.VisitationAdapter

class PlaceDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPlaceDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlaceDetailsBinding.inflate(inflater)

        setDefaults()
        initializeViews()
        initializeEvents()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setDefaults()
        initializeViews()
    }

    private fun setDefaults() {
        PlaceLogic.tmpPlaceId = requireArguments().getInt("place_id_for_place_details")
        PlaceLogic.tmpPlace = PlaceLogic.getPlaceById(dbOperation, PlaceLogic.tmpPlaceId)
    }

    private fun initializeViews() {
        (requireActivity() as MainActivity).changeMainActivityUI(
            setBackButtonVisible = true,
            titleString = PlaceLogic.tmpPlace.name,
            setViewPagerVisible = false,
            setTabLayoutVisible = true,
            setTabLayoutClickable = false,
            setBtnAddPlaceVisible = true,
            setBtnWideVisible = false
        )

        setPriorityImage()
        binding.tvDefinition.text = PlaceLogic.tmpPlace.definition
        binding.tvShortDescriptionMax3.text = PlaceLogic.tmpPlace.description
        setupRvVisitHistory()
    }

    private fun initializeEvents() {
        (requireActivity() as MainActivity).binding.includeTop.btnBack.setOnClickListener {
            val action =
                PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToPlacesToVisitFragment()
            findNavController().navigate(action)
        }

        binding.btnAddVisitation.setOnClickListener {
            Toast.makeText(requireContext(), "Function not implemented!", Toast.LENGTH_SHORT).show()
            /*
            val action =
                PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToAddVisitationFragment(
                    PlaceLogic.tmpPlaceId
                )
            findNavController().navigate(action)

             */
        }

        binding.btnShowLocation.setOnClickListener {
            Toast.makeText(requireContext(), "Function not implemented!", Toast.LENGTH_SHORT).show()
            //(requireActivity() as MainActivity).openMapsActivityFromPlaceDetailsFragment(PlaceLogic.tmpPlaceId)
        }

        (requireActivity() as MainActivity).binding.includeBottom.btnAddPlace.setOnClickListener {
            val action = PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToAddPlaceFragment()
            findNavController().navigate(action)
        }
    }

    private fun setPriorityImage() {
        when (PlaceLogic.tmpPlace.priority) {
            Priority.ONE -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_green)
            Priority.TWO -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_blue)
            Priority.THREE -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_gray)
        }
    }

    private fun setupRvVisitHistory() {
        VisitationLogic.listVisitation.clear()
        //VisitationLogic.listVisitation = PlaceLogic.getVisitationsOfPlace(PlaceLogic.tmpPlaceId, requireActivity())

        VisitationLogic.listVisitation.add(Visitation("2002.22.22", "description1", 1))
        VisitationLogic.listVisitation.add(Visitation("2002.22.22", "description1", 2))
        VisitationLogic.listVisitation.add(Visitation("2002.22.22", "description1", 3))
        VisitationLogic.listVisitation.add(Visitation("2002.22.22", "description1", 4))
        binding.rvVisitHistory.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvVisitHistory.adapter =
            VisitationAdapter(requireActivity(), VisitationLogic.listVisitation)
    }

}