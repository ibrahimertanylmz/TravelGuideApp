package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.turkcell.travelguideapp.databinding.CustomTabBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        initializeEvents()
        initializeViewPager()
        initializeTabs()
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

    fun initializeTabs(){

        TabLayoutMediator(binding.includeBottom.tabLayout, binding.viewpager){ tab, position->
        }.attach()

        val tabHome = CustomTabBinding.inflate(layoutInflater)
        tabHome.ivIcon.setImageResource(R.drawable.home_selector)
        tabHome.tvTab.text = getString(R.string.str_places_to_visit)

        val tabProfile = CustomTabBinding.inflate(layoutInflater)
        tabProfile.ivIcon.setImageResource(R.drawable.profile_selector)
        tabProfile.tvTab.text = getString(R.string.str_visited_places)

        binding.includeBottom.tabLayout.getTabAt(0)!!.setCustomView(tabHome.root)
        binding.includeBottom.tabLayout.getTabAt(1)!!.setCustomView(tabProfile.root)

    }

     fun initializeViewPager() {
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(PlacesToVisitFragment())
        adapter.addFragment(PlacesVisitedFragment())
        binding.viewpager.adapter = adapter
    }

    internal class ViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity)
    {
        private val fragmentList = ArrayList<Fragment>()

        override fun getItemCount(): Int {
            return fragmentList.size
        }
        override fun createFragment(position: Int): Fragment {
            return fragmentList.get(position)
        }
        fun addFragment(fragment: Fragment){
            fragmentList.add(fragment)
        }
    }
}