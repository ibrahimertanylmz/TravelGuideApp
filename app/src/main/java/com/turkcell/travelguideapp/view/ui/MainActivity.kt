package com.turkcell.travelguideapp.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.databinding.CustomTabBinding

lateinit var dbOperation: TravelGuideOperation

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    //private var ltlngData: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbOperation = TravelGuideOperation(this)

        initializeViews()
        initializeEvents()
        initializeViewPager()
        initializeTabs()

        PlaceLogic.debugTmpFillList(dbOperation)

    }

    private fun initializeViews() {
        changeMainActivityUI(
            setBackButtonVisible = false,
            "",
            setViewPagerVisible = true,
            setTabLayoutVisible = true,
            setBtnAddPlaceVisible = true,
            setBtnWideVisible = false
        )
    }

    private fun initializeEvents() {
        binding.includeTop.btnBack.setOnClickListener {
        }
        binding.includeBottom.btnAddPlace.setOnClickListener {
            intentToAddActivity()
        }
        binding.includeBottom.btnWide.setOnClickListener {
        }
    }

    fun intentToAddActivity() {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    fun intentToPlaceDetailsActivity(id: Int) {
        val intent = Intent(this, PlaceDetailsActivity::class.java)
        intent.putExtra("place_id_for_place_details", id)
        startActivity(intent)
    }

    fun changeMainActivityUI(
        setBackButtonVisible: Boolean,
        titleString: String = "",
        setViewPagerVisible: Boolean,
        setTabLayoutVisible: Boolean,
        setBtnAddPlaceVisible: Boolean,
        setBtnWideVisible: Boolean
    ) {
        if (setBackButtonVisible) {
            binding.includeTop.btnBack.visibility = View.VISIBLE
        } else {
            binding.includeTop.btnBack.visibility = View.INVISIBLE
        }
        if (titleString != "") {
            binding.includeTop.tvTopBarTitle.text = titleString
        }
        if (setViewPagerVisible) {
            binding.viewpager.visibility = View.VISIBLE
            binding.fragmentContainer.visibility = View.INVISIBLE
        } else {
            binding.viewpager.visibility = View.INVISIBLE
            binding.fragmentContainer.visibility = View.VISIBLE
        }
        if (setTabLayoutVisible) {
            binding.includeBottom.tabLayout.visibility = View.VISIBLE
        } else {
            binding.includeBottom.tabLayout.visibility = View.INVISIBLE
        }
        if (setBtnAddPlaceVisible) {
            binding.includeBottom.llBottom.visibility = View.VISIBLE
        } else {
            binding.includeBottom.llBottom.visibility = View.INVISIBLE
        }
        if (setBtnWideVisible) {
            binding.includeBottom.btnWide.visibility = View.VISIBLE
        } else {
            binding.includeBottom.btnWide.visibility = View.INVISIBLE
        }
    }

    private fun initializeTabs() {

        TabLayoutMediator(binding.includeBottom.tabLayout, binding.viewpager) { _, _ ->
        }.attach()

        val tabHome = CustomTabBinding.inflate(layoutInflater)
        tabHome.ivIcon.setImageResource(R.drawable.home_selector)
        tabHome.tvTab.text = getString(R.string.str_places_to_visit)

        val tabProfile = CustomTabBinding.inflate(layoutInflater)
        tabProfile.ivIcon.setImageResource(R.drawable.profile_selector)
        tabProfile.tvTab.text = getString(R.string.str_visited_places)

        binding.includeBottom.tabLayout.getTabAt(0)!!.customView = tabHome.root
        binding.includeBottom.tabLayout.getTabAt(1)!!.customView = tabProfile.root

    }

    private fun initializeViewPager() {
        val adapter = ViewPagerAdapter(this)

        adapter.addFragment(PlacesToVisitFragment())
        adapter.addFragment(PlacesVisitedFragment())
        binding.viewpager.adapter = adapter
    }

    internal class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        private val fragmentList = ArrayList<Fragment>()

        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }
    }

    override fun onBackPressed() {
    }
}