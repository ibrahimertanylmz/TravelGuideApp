package com.turkcell.travelguideapp.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.databinding.FragmentAddPlaceBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter

class AddPlaceFragment : Fragment() {
    private lateinit var binding: FragmentAddPlaceBinding

    private lateinit var photoList: ArrayList<Any>

    //lateinit var fotoğrafEkleView: ImageView
    private var priority: Priority = Priority.THREE
    private var getLocation: LatLng? = null

    private var getLatitude: Double? = null
    private var getLongitude: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddPlaceBinding.inflate(inflater)

        (requireActivity() as MainActivity).changeBackButtonVisibility(true)
        (requireActivity() as MainActivity).changeTabLayoutVisibility(false)

        getIntent()
        spinnerListOperations()
        getLatitudeAndLongitude()


        /*if(photoList.size>=10){
            fotoğrafEkleView.visibility=View.GONE
        }else{
            fotoğrafEkleView.visibility=View.VISIBLE
            fotoğrafEkleView.setOnClickListener {

                //open gallery

                photoList.add(Visitation("sad", "asd"))

            }
        }
*/
        initLm()

        binding.btnSave.setOnClickListener {
            btnSaveOnClick()
        }

        binding.btnAddLocation.setOnClickListener {
            btnAddLocationOnClick()
        }

        return binding.root
    }

    private fun initLm() {
        val lm = LinearLayoutManager(requireContext())

        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotos.layoutManager = lm
        binding.rwPhotos.adapter =
            PhotoAdapter(
                requireContext(),
                photoList,
                ::itemClick,
                ::itemButtonClick,
                ::itemAddPhotoClick
            )
    }

    private fun btnAddLocationOnClick() {
        (activity as MainActivity).openMapsActivityFromAddPlaceFragment()

    }

    // Maps activity'den gelen latitude ile longitude'yi alıyoruz
    private fun getIntent() {
        val intent = Intent()

        getLatitude = intent.getDoubleExtra("fromMapsLocationLatitude", 0.0)
        getLongitude = intent.getDoubleExtra("fromMapsLocationLongitude", 0.0)
    }

    private fun btnSaveOnClick() {

        if (getLatitude != null && getLongitude != null) {
            getLocation = LatLng(getLatitude!!, getLongitude!!)

            lateinit var p: Place

            if (getLocation != null) {
                //LatLng patlayabilir
                p = Place(
                    binding.edtPlaceName.text.toString(),
                    getLocation!!,
                    binding.edtDefinition.text.toString(),
                    binding.edtDescription.text.toString(),
                    priority
                )

                PlaceLogic.addPlace(requireContext(), p)
            } else {
                val adb= AlertDialog.Builder(requireContext())
                adb.setTitle(getString(R.string.LOCATION))
                adb.setMessage(getString(R.string.add_place_fragment_choose_location_confirmation))
                adb.setPositiveButton(getString(R.string.Confirm)) { _, _ ->
                    //maps activity aç
                    (activity as MainActivity).openMapsActivityFromAddPlaceFragment()
                }
                adb.setNegativeButton(getString(R.string.No),null)
                adb.show()

            }
        }
    }


    private fun getLatitudeAndLongitude() {
        // aktiviteye gelen intent bilgilerini kullanıyoruz
        getLatitude = (activity as MainActivity).latitudeData
        getLongitude = (activity as MainActivity).longitudeData


    }

    private fun spinnerListOperations() {
        val liste = arrayListOf("Öncelik Seç", "Öncelik 1", "Öncelik 2", "Öncelik 3")

        val adap =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, liste)
        binding.spinnerPriority.adapter = adap

        binding.spinnerPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    when (p2) {
                        1 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_green)
                            priority = Priority.ONE //  !!!Bu şekilde olur mu emin değilim!!!
                        }
                        2 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_blue)
                            priority = Priority.TWO
                        }
                        3 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_gray)
                            priority = Priority.THREE
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
            }
        // adap.notifyDataSetChanged()
    }


    private fun itemClick(position: Int) {
        //optional
    }

    private fun itemButtonClick(position: Int) {
        //dbDelete(position)
    }

    private fun itemAddPhotoClick(position: Int) {
        //open gallery and select photo
    }
}