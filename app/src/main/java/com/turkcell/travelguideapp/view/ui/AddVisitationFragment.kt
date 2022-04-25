package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.turkcell.travelguideapp.databinding.FragmentAddVisitationBinding

class AddVisitationFragment : Fragment() {
    private lateinit var binding: FragmentAddVisitationBinding

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

        return binding.root
    }
}