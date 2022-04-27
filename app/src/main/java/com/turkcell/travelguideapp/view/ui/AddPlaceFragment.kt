package com.turkcell.travelguideapp.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.MapLogic
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.databinding.FragmentAddPlaceBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority

class AddPlaceFragment : Fragment() {

    private lateinit var binding: FragmentAddPlaceBinding

    private var priority: Priority = Priority.THREE
    private var photoList = ArrayList<Any>()
    //lateinit var fotoğrafEkleView: ImageView
    private var getLocation: LatLng? = null
    private var getLatitude: Double? = null
    private var getLongitude: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddPlaceBinding.inflate(inflater)

        initializeViews()
        initializeEvents()

        spinnerListOperations()

        //initLm()

        /*
        if(photoList.size>=10){
            fotoğrafEkleView.visibility=View.GONE
        }else{
            fotoğrafEkleView.visibility=View.VISIBLE
            fotoğrafEkleView.setOnClickListener {

                //open gallery

                photoList.add(Visitation("sad", "asd"))

            }
        }
         */

        return binding.root
    }

    private fun initializeViews() {
        (requireActivity() as MainActivity).changeMainActivityHuds(
            setBackButtonVisible = true,
            setTabLayoutVisibleAndBtnWideInvisible = false,
            setViewPagerVisible = false,
            titleString = getString(R.string.Add_Place)
        )
    }

    private fun initializeEvents() {
        (requireActivity() as MainActivity).binding.includeTop.btnBack.setOnClickListener {
            val action = AddPlaceFragmentDirections.actionAddPlaceFragmentToPlacesToVisitFragment()
            findNavController().navigate(action)
        }

        binding.btnAddLocation.setOnClickListener {
            btnAddLocationOnClick()
        }

        (activity as MainActivity).binding.includeBottom.btnWide.setOnClickListener {
            btnSaveOnClick()
        }
    }

    private fun btnSaveOnClick() {
        lateinit var p: Place
        //Definition and photos are optional
        if (binding.edtPlaceName.text.toString().isNotEmpty()) {
            if (binding.edtDescription.text.toString().isNotEmpty()) {
                if (MapLogic.tmpMap.lat != 0.0 && MapLogic.tmpMap.long != 0.0) {
                    p = Place(
                        binding.edtPlaceName.text.toString(),
                        LatLng(MapLogic.tmpMap.lat, MapLogic.tmpMap.long),
                        binding.edtDefinition.text.toString(),
                        binding.edtDescription.text.toString(),
                        priority
                    )
                    PlaceLogic.addPlace(requireContext(), p)
                    Toast.makeText(requireContext(), getString(R.string.place_added_successfuly), Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val adb = AlertDialog.Builder(requireContext())
                    adb.setTitle(getString(R.string.LOCATION))
                    adb.setMessage(getString(R.string.add_place_fragment_choose_location_confirmation))
                    adb.setPositiveButton(getString(R.string.Confirm)) { _, _ ->
                        (activity as MainActivity).openMapsActivityFromAddPlaceFragment()
                    }
                    adb.setNegativeButton(getString(R.string.No), null)
                    adb.show()
                }
            } else {
                binding.edtDescription.error = getString(R.string.This_field_cannot_be_empty)
            }
        } else {
            binding.edtPlaceName.error = getString(R.string.This_field_cannot_be_empty)
        }
    }

    private fun btnAddLocationOnClick() {
        (activity as MainActivity).openMapsActivityFromAddPlaceFragment()
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
                            priority = Priority.ONE
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
    }


    //TO-DO:
    //photo recyclerview functions
    /*
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

    private fun itemClick(position: Int) {
        //optional
    }

    private fun itemButtonClick(position: Int) {
        //dbDelete(position)
    }

    private fun itemAddPhotoClick(position: Int) {
        //open gallery and select photo
    }
     */
}