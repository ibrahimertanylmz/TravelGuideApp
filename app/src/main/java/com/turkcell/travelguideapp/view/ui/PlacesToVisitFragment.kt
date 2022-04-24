package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.databinding.FragmentPlacesToVisitBinding

class PlacesToVisitFragment : Fragment() {

    private lateinit var binding: FragmentPlacesToVisitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //val args: PlacesToVisitFragment by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlacesToVisitBinding.inflate(inflater)

        //val action = PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToAddPlaceFragment(15)
        //findNavController().navigate(action)

        return binding.root
    }

    fun itemClick(){
        val action = PlacesToVisitFragmentDirections.actionPlacesToVisitFragmentToPlaceDetailsFragment(-1)
        findNavController().navigate(action)
    }

}