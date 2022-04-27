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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddPlaceBinding.inflate(inflater)

        initializeViews()
        initializeEvents()

        return binding.root
    }

    private fun initializeViews() {
        (requireActivity() as MainActivity).changeMainActivityUI(
            setBackButtonVisible = true,
            titleString = getString(R.string.Add_Place),
            setViewPagerVisible = false,
            setTabLayoutVisible = false,
            setTabLayoutClickable = false,
            setBtnAddPlaceVisible = false,
            setBtnWideVisible = true
        )

        spinnerListOperations()
    }

    private fun initializeEvents() {
        (requireActivity() as MainActivity).binding.includeTop.btnBack.setOnClickListener {
            val action = AddPlaceFragmentDirections.actionAddPlaceFragmentToPlacesToVisitFragment()
            findNavController().navigate(action)
        }

        binding.btnAddLocation.setOnClickListener {
            Toast.makeText(requireContext(), "Function not implemented!", Toast.LENGTH_SHORT).show()
            //(activity as MainActivity).openMapsActivityFromAddPlaceFragment()
        }

        (activity as MainActivity).binding.includeBottom.btnWide.setOnClickListener {
            addCurrentPlaceToDb()
        }
    }

    private fun addCurrentPlaceToDb() {
        lateinit var p: Place
        if (binding.edtPlaceName.text.toString().isNotEmpty()) {
            if (binding.edtDescription.text.toString().isNotEmpty()) {
                if (MapLogic.tmpMap.lat != 0.0 && MapLogic.tmpMap.long != 0.0) {
                    p = Place(
                        binding.edtPlaceName.text.toString(),
                        LatLng(MapLogic.tmpMap.lat, MapLogic.tmpMap.long),
                        binding.edtDefinition.text.toString(),
                        binding.edtDescription.text.toString(),
                        priority,
                        ""
                    )
                    PlaceLogic.addPlace(requireContext(), p)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.place_added_successfuly),
                        Toast.LENGTH_SHORT
                    ).show()
                    val action = AddPlaceFragmentDirections.actionAddPlaceFragmentToPlacesToVisitFragment()
                    findNavController().navigate(action)
                    requireActivity().finish()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)

                } else {
                    val adb = AlertDialog.Builder(requireContext())
                    adb.setTitle(getString(R.string.LOCATION))
                    adb.setMessage(getString(R.string.add_place_fragment_choose_location_confirmation))
                    adb.setPositiveButton(getString(R.string.Confirm)) { _, _ ->
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.function_not_implemented),
                            Toast.LENGTH_SHORT
                        ).show()
                        //(activity as MainActivity).openMapsActivityFromAddPlaceFragment()
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


    private fun spinnerListOperations() {
        val list = arrayListOf(getString(R.string.Choose_Priority), getString(R.string.Priority_1), getString(
                    R.string.Priority_2), getString(R.string.Priority_3))

        val adapt =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, list)
        binding.spinnerPriority.adapter = adapt

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

}