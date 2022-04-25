package com.turkcell.travelguideapp.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.BLL.VisitationLogic.visitationList
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.databinding.FragmentPlaceDetailsBinding

class PlaceDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPlaceDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlaceDetailsBinding.inflate(inflater)

        val liste= arrayListOf<String>("Eleman1","Eleman2","Eleman3","Eleman4","Eleman5","Eleman6","Eleman1","Eleman2","Eleman3","Eleman4","Eleman5","Eleman6","Eleman1","Eleman2","Eleman3","Eleman4","Eleman5","Eleman6")

        //Adapter= liste yapısının içeriğinin doldurulması ve yönetilmesi işlemini üstlenir.
        var adap= ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,liste)
        binding.spinner.adapter=adap
        adap.notifyDataSetChanged()// işlemler yapıldıktan sonra kullanılır, adapter'in yenilenmesini sağlar böylece yeni elemanlar gözükür

        setupRvVisitHistory()
        initializeEvents()

        return binding.root
    }

    private fun initializeEvents() {
        binding.btnAddVisitation.setOnClickListener {
            val action = PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToAddVisitationFragment()
            requireView().findNavController().navigate(action)
        }

        binding.btnShowLocation.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            //buradaki id nereden gelecek?
            intent.putExtra("placeId", -1)
            startActivity(intent)
        }
    }

    private fun setupRvVisitHistory() {
        binding.rvVisitHistory.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvVisitHistory.adapter = VisitationAdapter(requireActivity(), visitationList)
    }

}