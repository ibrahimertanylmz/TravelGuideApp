package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.turkcell.travelguideapp.databinding.FragmentAddPlaceBinding

class AddPlaceFragment : Fragment() {
    private lateinit var binding: FragmentAddPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddPlaceBinding.inflate(inflater)

        (requireActivity() as MainActivity).changeBackButtonVisibility(true)
        (requireActivity() as MainActivity).changeTabLayoutVisibility(false)

        return binding.root
    }
}