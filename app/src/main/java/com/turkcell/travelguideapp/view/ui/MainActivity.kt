package com.turkcell.travelguideapp.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.databinding.ActivityMainBinding
import com.turkcell.travelguideapp.model.Priority

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        initializeEvents()
    }

    private fun initializeViews() {
        changeBackButtonVisibility(false)
        changeTabLayoutVisibility(true)
    }

    private fun initializeEvents() {
        binding.includeBottom.btnAddPlace.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = AddPlaceFragment()
            fragmentTransaction.replace(R.id.fragment, fragment).commit()
        }
    }

    fun changeBackButtonVisibility(setBackButtonVisible: Boolean) {
        if (setBackButtonVisible) {
            binding.includeTop.btnBack.visibility = View.VISIBLE
        } else {
            binding.includeTop.btnBack.visibility = View.INVISIBLE
        }
    }

    fun changeTabLayoutVisibility(setTabLayoutVisible: Boolean) {
        if (setTabLayoutVisible) {
            binding.includeBottom.tabLayout.visibility = View.VISIBLE
            binding.includeBottom.llBottom.visibility = View.VISIBLE
            binding.includeBottom.btnWide.visibility = View.INVISIBLE
        } else {
            binding.includeBottom.tabLayout.visibility = View.INVISIBLE
            binding.includeBottom.llBottom.visibility = View.INVISIBLE
            binding.includeBottom.btnWide.visibility = View.VISIBLE
        }
    }
}