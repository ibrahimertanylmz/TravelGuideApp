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
import com.turkcell.travelguideapp.databinding.FragmentAddVisitationBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter

class AddVisitationFragment : Fragment() {
    private lateinit var binding: FragmentAddVisitationBinding

    private var placeId: Int = -1
    private lateinit var currentPlace: Place

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddVisitationBinding.inflate(inflater)

        setDefaults()
        initializeViews()
        initializeEvents()

        return binding.root
    }

    private fun setDefaults() {
        placeId = requireArguments().getInt("place_id_for_add_visitation")
        currentPlace = PlaceLogic.getPlaceById(dbOperation, placeId)
    }

    private fun initializeViews() {
        /*
        (requireActivity() as MainActivity).changeMainActivityUI(
            setBackButtonVisible = true,
            setTabLayoutVisibleAndBtnWideInvisible = false,
            setViewPagerVisible = false,
            titleString = currentPlace.name
        )

         */

        //ne işe yaradığına bak??
        //initLm()
    }

    private fun initializeEvents() {
        (requireActivity() as MainActivity).binding.includeTop.btnBack.setOnClickListener {
            val action =
                AddVisitationFragmentDirections.actionAddVisitationFragmentToPlaceDetailsFragment(
                    placeId
                )
            findNavController().navigate(action)
        }

        (requireActivity() as MainActivity).binding.includeBottom.btnWide.setOnClickListener {
            if (binding.edtVisitDate.text.toString() != "" && binding.edtVisitDesc.text.toString() != "") {
                val tmpVisitation = Visitation(
                    binding.edtVisitDate.text.toString(),
                    binding.edtVisitDesc.text.toString(),
                    placeId
                )
                VisitationLogic.addVisitation(dbOperation, tmpVisitation)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_all_the_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initLm() {
        val lm = LinearLayoutManager(requireContext())

        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotosVisitation.layoutManager = lm
        binding.rwPhotosVisitation.adapter = PhotoAdapter(
            requireContext(),
            currentPlace.imageList,
            ::itemClick,
            ::itemButtonClick,
            ::itemAddPhotoClick
        )
    }

    //recyclerview için gelen fonksiyonlar boş
    fun itemClick(position: Int) {
        //optional
    }

    fun itemButtonClick(position: Int) {
        //dbDelete(position)
    }

    fun itemAddPhotoClick(position: Int) {
        //open gallery and select photo
    }
}