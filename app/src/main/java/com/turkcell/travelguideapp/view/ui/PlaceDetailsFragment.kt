package com.turkcell.travelguideapp.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.bll.VisitationLogic
import com.turkcell.travelguideapp.databinding.FragmentPlaceDetailsBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.VisitationAdapter

class PlaceDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPlaceDetailsBinding

    private var placeId: Int = -1
    private lateinit var currentPlace: Place

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
        initializeViews()
    }

    private fun setDefaults() {
        placeId = requireArguments().getInt("place_id_for_place_details")
        currentPlace = PlaceLogic.getPlaceById(dbOperation, placeId)
    }

    private fun initializeViews() {
        (requireActivity() as MainActivity).changeMainActivityHuds(
            setBackButtonVisible = true,
            setTabLayoutVisibleAndBtnWideInvisible = true,
            setViewPagerVisible = false,
            titleString = currentPlace.name
        )

        setPriorityImage()

        binding.tvDefinition.text = currentPlace.definition
        binding.tvShortDescriptionMax3.text = currentPlace.description

        setupRvVisitHistory()
    }

    private fun initializeEvents() {
        (requireActivity() as MainActivity).binding.includeTop.btnBack.setOnClickListener {
            val action = PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToPlacesToVisitFragment()
            findNavController().navigate(action)
        }

        binding.btnAddVisitation.setOnClickListener {
            val action = PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToAddVisitationFragment(placeId)
            findNavController().navigate(action)
        }

        binding.btnShowLocation.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            intent.putExtra("placeId", placeId)
            startActivity(intent)
        }
    }

    private fun setPriorityImage() {
        when (currentPlace.priority) {
            Priority.ONE -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_green)
            Priority.TWO -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_blue)
            Priority.THREE -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_gray)
        }
    }

    private fun setupRvVisitHistory() {
        //binding.rvVisitHistory.isNestedScrollingEnabled = false
        //binding.rvVisitHistory.setHasFixedSize(false)
        VisitationLogic.listVisitation.clear()
        VisitationLogic.listVisitation = PlaceLogic.getVisitationsOfPlace(placeId, requireActivity())

        val list = ArrayList<Visitation>()
        list.add(Visitation("2002.22.22", "description1", 1))
        list.add(Visitation("2002.22.22", "description1", 2))
        list.add(Visitation("2002.22.22", "description1", 3))
        list.add(Visitation("2002.22.22", "description1", 4))
        binding.rvVisitHistory.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        //binding.rvVisitHistory.adapter = VisitationAdapter(requireActivity(), VisitationLogic.listVisitation)
        binding.rvVisitHistory.adapter = VisitationAdapter(requireActivity(), list)
    }

}