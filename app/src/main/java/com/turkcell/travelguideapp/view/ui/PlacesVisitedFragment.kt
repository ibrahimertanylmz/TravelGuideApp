package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.turkcell.travelguideapp.databinding.FragmentPlacesVisitedBinding


class PlacesVisitedFragment : Fragment() {
    private lateinit var binding: FragmentPlacesVisitedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlacesVisitedBinding.inflate(inflater)

        return binding.root
    }

    fun itemClick(position: Int) {
        val action =
            PlacesVisitedFragmentDirections.actionPlacesVisitedFragmentToPlaceDetailsFragment(
                position
            )
        findNavController().navigate(action)
    }

}