package com.turkcell.travelguideapp.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.bll.VisitationLogic
import com.turkcell.travelguideapp.databinding.ActivityPlaceDetailsBinding
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.VisitationAdapter

class PlaceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaults()
        initializeViews()
        initializeEvents()
    }

    override fun onResume() {
        super.onResume()
        //setDefaults()
        //initializeViews()
    }

    private fun setDefaults() {
        PlaceLogic.tmpPlaceId = intent.getIntExtra("place_id_for_place_details", -1)
        PlaceLogic.tmpPlace = PlaceLogic.getPlaceById(dbOperation, PlaceLogic.tmpPlaceId)
    }

    private fun initializeViews() {
        binding.includeTop.tvTopBarTitle.text = PlaceLogic.tmpPlace.name
        binding.includeBottom.tabLayout.visibility = View.INVISIBLE
        binding.includeBottom.llBottom.visibility = View.INVISIBLE
        binding.includeBottom.btnWide.visibility = View.INVISIBLE

        setPriorityImage()
        binding.tvDefinition.text = PlaceLogic.tmpPlace.definition
        binding.tvShortDescriptionMax3.text = PlaceLogic.tmpPlace.description
        setupRvVisitHistory()
    }

    private fun initializeEvents() {
        binding.includeTop.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnAddVisitation.setOnClickListener {
            val intent = Intent(this, AddVisitationActivity::class.java)
            intent.putExtra("place_id_for_add_visitation", PlaceLogic.tmpPlaceId)
            startActivity(intent)
        }

        binding.btnShowLocation.setOnClickListener {
            Toast.makeText(this, "Function not implemented!", Toast.LENGTH_SHORT).show()
            //(requireActivity() as MainActivity).openMapsActivityFromPlaceDetailsFragment(PlaceLogic.tmpPlaceId)
        }


        binding.includeBottom.btnAddPlace.setOnClickListener {
            Toast.makeText(this, "Function not implemented!", Toast.LENGTH_SHORT).show()
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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVisitHistory.adapter =
            VisitationAdapter(this, VisitationLogic.listVisitation)
    }
}