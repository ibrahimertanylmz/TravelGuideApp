package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.VisitationLogic
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.databinding.FragmentAddVisitationBinding
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter

class AddVisitationFragment : Fragment() {
    private lateinit var binding: FragmentAddVisitationBinding
    private lateinit var photoList: ArrayList<Any>
    var placeId: Int = -1
    lateinit var dbOperation: TravelGuideOperation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddVisitationBinding.inflate(inflater)

        (requireActivity() as MainActivity).changeBackButtonVisibility(true)
        (requireActivity() as MainActivity).changeTabLayoutVisibility(false)

        placeId = requireArguments().getInt("place_id_for_add_visitation")

        dbOperation = TravelGuideOperation(requireContext())

        (requireActivity() as MainActivity).binding.includeBottom.btnWide.setOnClickListener {
            if (binding.edtVisitDate.text.toString() != "" && binding.edtVisitDesc.text.toString() != "") {
                val tmpVisitation = Visitation(
                    binding.edtVisitDate.text.toString(),
                    binding.edtVisitDesc.text.toString()
                )
                VisitationLogic.addVisitation(dbOperation, tmpVisitation, placeId)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_all_the_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        initLm()

        return binding.root
    }

    fun initLm() {
        val lm = LinearLayoutManager(requireContext())

        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotosVisitation.layoutManager = lm
        binding.rwPhotosVisitation.adapter = PhotoAdapter(
            requireContext(),
            photoList,
            ::itemClick,
            ::itemButtonClick,
            ::itemAddPhotoClick
        )
    }

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