package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.databinding.FragmentAddPlaceBinding
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter

class AddPlaceFragment : Fragment() {
    private lateinit var binding: FragmentAddPlaceBinding

    private lateinit var photoList: ArrayList<Visitation>
    lateinit var fotoğrafEkleView: ImageView

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

        if (photoList.size >= 10) {
            fotoğrafEkleView.visibility = View.GONE
        } else {
            fotoğrafEkleView.visibility = View.VISIBLE
            fotoğrafEkleView.setOnClickListener {

                //open gallery

                photoList.add(Visitation("sad", "asd"))

            }
        }

        initLm()

        return binding.root
    }

    fun initLm() {
        val lm = LinearLayoutManager(requireContext())

        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotos.layoutManager = lm
        binding.rwPhotos.adapter =
            PhotoAdapter(
                requireContext(),
                photoList,
                ::itemClick,
                ::itemButtonClick,
                ::itemAddPhotoClick
            )
    }

    fun itemClick(position: Int) {
        //optional
    }

    fun itemButtonClick(position: Int) {
        //dbDelete(position)
    }

    fun itemAddPhotoClick(position: Int) {
        //open gallery and select photo
    }
}