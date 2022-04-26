package com.turkcell.travelguideapp.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.databinding.FragmentPlaceDetailsBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.VisitationAdapter

class PlaceDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPlaceDetailsBinding
    private var placeId: Int = -1
    lateinit var visitationList: ArrayList<Visitation>
    lateinit var currentPlace: Place
    lateinit var dbOperation: TravelGuideOperation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlaceDetailsBinding.inflate(inflater)


        dbOperation = TravelGuideOperation(requireContext())

        placeId = requireArguments().getInt("place_id_for_place_details")

        initializeViews()
        initializeEvents()

        return binding.root
    }

    private fun initializeViews() {
        currentPlace = PlaceLogic.getPlaceById(dbOperation, placeId)

        setPriorityImage()

        binding.tvDefinition.text = currentPlace.definition
        binding.tvShortDescriptionMax3.text = currentPlace.description

        setupRvVisitHistory()
    }

    private fun setPriorityImage() {
        when (currentPlace.priority) {
            Priority.ONE -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_green)
            Priority.TWO -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_blue)
            Priority.THREE -> binding.imageViewPriority.setImageResource(R.drawable.rv_oval_item_gray)
        }
    }

    private fun initializeEvents() {
        binding.btnAddVisitation.setOnClickListener {
            /*
            val action =
                PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToAddVisitationFragment(
                    placeId
                )
            requireView().findNavController().navigate(action)

             */
        }

        binding.btnShowLocation.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            intent.putExtra("placeId", placeId)
            startActivity(intent)
        }
    }

    private fun setupRvVisitHistory() {
        visitationList = PlaceLogic.getVisitationsOfPlace(placeId, requireActivity())
        binding.rvVisitHistory.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvVisitHistory.adapter = VisitationAdapter(requireActivity(), visitationList)
    }

}