package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
    ): View? {

        binding = FragmentPlaceDetailsBinding.inflate(inflater)


        val liste= arrayListOf<String>("Eleman1","Eleman2","Eleman3","Eleman4","Eleman5","Eleman6","Eleman1","Eleman2","Eleman3","Eleman4","Eleman5","Eleman6","Eleman1","Eleman2","Eleman3","Eleman4","Eleman5","Eleman6")

        //Adapter= liste yapısının içeriğinin doldurulması ve yönetilmesi işlemini üstlenir.
        var adap= ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,liste)
        binding.spinner.adapter=adap
        adap.notifyDataSetChanged()// işlemler yapıldıktan sonra kullanılır, adapter'in yenilenmesini sağlar böylece yeni elemanlar gözükür



        return binding.root
    }
}