package com.turkcell.travelguideapp.view.ui

import android.graphics.Bitmap
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.ImageLogic
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.bll.VisitationLogic
import com.turkcell.travelguideapp.databinding.ActivityPlaceDetailsBinding
import com.turkcell.travelguideapp.databinding.PopUpFullscreenImageBinding
import me.relex.circleindicator.CircleIndicator3
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.DetailSlideAdapter
import com.turkcell.travelguideapp.view.adapter.VisitationAdapter

class PlaceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailsBinding
    private var tmpPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaults()
        initializeViews()
        initializeEvents()
        initializeViewPager()

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_SHORT).show()

                tmpPosition = position
            }
        })

    }

    override fun onResume() {
        super.onResume()
        PlaceLogic.tmpPlace = PlaceLogic.getPlaceById(dbOperation, PlaceLogic.tmpPlaceId)
        initializeViews()
        initializeViewPager()
        //setDefaults()
        //initializeViews()
    }

    private fun setDefaults() {
        PlaceLogic.tmpPlaceId = intent.getIntExtra("place_id_for_place_details", -1)

        println()
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

    private fun initializeViewPager() {
        postToList()

        PlaceLogic.tmpPlace.imageList = ImageLogic.getImagesByPlaceId(this, PlaceLogic.tmpPlaceId)

        binding.viewPager2.adapter = DetailSlideAdapter(PlaceLogic.tmpPlace.imageList, ::itemClick)

        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(binding.viewPager2)
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
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("placeId", PlaceLogic.tmpPlaceId)
            startActivity(intent)
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
        VisitationLogic.listVisitation = PlaceLogic.getVisitationsOfPlace(PlaceLogic.tmpPlaceId, this)
        val liste = PlaceLogic.getVisitationsOfPlace(PlaceLogic.tmpPlaceId, this)
        binding.rvVisitHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVisitHistory.adapter =
            VisitationAdapter(this, liste)
        binding.rvVisitHistory.adapter!!.notifyItemChanged(-1)
    }

    fun itemClick(i: Int) {
        showPopUp(i)
    }

    private fun addToList(image: Bitmap) {
        PlaceLogic.tmpPlace.imageList.add(image)
    }

    private fun postToList() {
        for (i in 1..PlaceLogic.tmpPlace.imageList.size) {
            addToList(PlaceLogic.tmpPlace.imageList[i])
            Toast.makeText(applicationContext, i.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPopUp(a: Int) {
        val binding: PopUpFullscreenImageBinding =
            PopUpFullscreenImageBinding.inflate(layoutInflater)
        val v = binding.root
        val popAlert =
            PopupWindow(v, windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
        popAlert.showAtLocation(v, Gravity.CENTER, 0, 0)
        var tmpImageList = ImageLogic.getImagesByPlaceId(this, PlaceLogic.tmpPlaceId)
        val currentImageBitmap: Bitmap = tmpImageList[0]
        binding.ivFullscreen.setImageBitmap(currentImageBitmap)
        v.setOnClickListener {
            popAlert.dismiss()
        }
    }
}
