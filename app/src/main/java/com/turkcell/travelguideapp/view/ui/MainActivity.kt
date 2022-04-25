package com.turkcell.travelguideapp.view.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.databinding.ActivityMainBinding
import com.turkcell.travelguideapp.databinding.CustomTabBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerOlustur()
        tabOlustur()

    }

    fun tabOlustur(){

        TabLayoutMediator(binding.includeBottom.tabLayout, binding.viewpager){ tab, position->
        }.attach()

        val tabHome = CustomTabBinding.inflate(layoutInflater)
        tabHome.ivIcon.setImageResource(R.drawable.home_selector)
        tabHome.tvTab.text = "Gezilecekler"

        val tabProfile = CustomTabBinding.inflate(layoutInflater)
        tabProfile.ivIcon.setImageResource(R.drawable.profile_selector)
        tabProfile.tvTab.text = "Gezdiklerim"

        binding.includeBottom.tabLayout.getTabAt(0)!!.setCustomView(tabHome.root)
        binding.includeBottom.tabLayout.getTabAt(1)!!.setCustomView(tabProfile.root)


    }

     fun viewPagerOlustur() {
        val adapter = ViewPagerAdapter(this)
        adapter.fragmentEkle(PlacesToVisitFragment())
        adapter.fragmentEkle(PlacesVisitedFragment())
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
        fun fragmentEkle(fragment: Fragment){
            fragmentList.add(fragment)
        }
    }

}