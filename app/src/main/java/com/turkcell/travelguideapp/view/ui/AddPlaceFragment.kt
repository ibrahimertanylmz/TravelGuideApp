package com.turkcell.travelguideapp.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.databinding.FragmentAddPlaceBinding
import com.turkcell.travelguideapp.model.Priority
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

        spinnerListOperations()


        if(photoList.size>=10){
            fotoğrafEkleView.visibility=View.GONE
        }else{
            fotoğrafEkleView.visibility=View.VISIBLE
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

    fun spinnerListOperations(){
        val liste= arrayListOf<String>("Öncelik Seç","Öncelik 1","Öncelik 2","Öncelik 3")

        val adap= ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,liste)
        binding.spinnerPriority.adapter=adap

        binding.spinnerPriority.onItemSelectedListener=object: AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                when (p2) {
                    1 -> binding.imageCircle.setImageResource(R.drawable.rv_oval_item_green)
                    2 -> binding.imageCircle.setImageResource(R.drawable.rv_oval_item_blue)
                    3 -> binding.imageCircle.setImageResource(R.drawable.rv_oval_item_gray)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
        }
       // adap.notifyDataSetChanged()
    }


    fun itemClick(position:Int){
        //optional
    }

    fun itemButtonClick(position: Int) {
        //dbDelete(position)
    }

    fun itemAddPhotoClick(position: Int) {
        //open gallery and select photo
    }
}