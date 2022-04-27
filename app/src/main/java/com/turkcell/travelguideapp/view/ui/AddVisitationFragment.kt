package com.turkcell.travelguideapp.view.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.ImageLogic
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.bll.VisitationLogic
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.databinding.FragmentAddVisitationBinding
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter
import java.io.File
import java.io.IOException

class AddVisitationFragment : Fragment() {
    private lateinit var binding: FragmentAddVisitationBinding
    private var photoList = ArrayList<Bitmap>()
    private var placeId: Int = -1
    private lateinit var dbOperation: TravelGuideOperation
    val requestCodeGallery = 1001
    lateinit var resimUri: Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddVisitationBinding.inflate(inflater)

        placeId = requireArguments().getInt("place_id_for_add_visitation")

        dbOperation = TravelGuideOperation(requireContext())

        (requireActivity() as MainActivity).binding.includeTop.tvTopBarTitle.text = "BOŞ, DÜZENLE"
        (requireActivity() as MainActivity).changeBackButtonVisibility(true)
        (requireActivity() as MainActivity).changeTabLayoutVisibility(false)
        (requireActivity() as MainActivity).changeViewPagerVisibility(false)
        (requireActivity() as MainActivity).binding.includeTop.btnBack.setOnClickListener {
            val action = AddVisitationFragmentDirections.actionAddVisitationFragmentToPlaceDetailsFragment(placeId)
            findNavController().navigate(action)
        }

        (requireActivity() as MainActivity).binding.includeBottom.btnWide.setOnClickListener {
            if (binding.edtVisitDate.text.toString() != "" && binding.edtVisitDesc.text.toString() != "") {
                val tmpVisitation = Visitation(
                    binding.edtVisitDate.text.toString(),
                    binding.edtVisitDesc.text.toString(),
                    placeId
                )
                VisitationLogic.addVisitation(dbOperation, tmpVisitation)
                photoList.forEach {
                    ImageLogic.addImage(requireContext(),it,placeId)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_all_the_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        initLm()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkGalleryPermissions(){
        val requestList = ImageLogic.galeriIzinKontrol(requireContext())
        if(requestList.size == 0){
            openGallery()
        }else{
            requestPermissions(requestList.toTypedArray(), requestCodeGallery)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var tumuOnaylandi = true
        for( gr in grantResults){
            if (gr != PackageManager.PERMISSION_GRANTED){
                tumuOnaylandi = false
                break
            }
        }
        if(!tumuOnaylandi){
            var tekrarGosterme = false
            for(permission in permissions){
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)){  }
                else if(ContextCompat.checkSelfPermission(requireContext(), permission)== PackageManager.PERMISSION_GRANTED){ }
                else{
                    tekrarGosterme = true
                    break
                }
            }

            if (tekrarGosterme){ ImageLogic.showAlert(requireContext())}
        }else{
            when(requestCode){
                requestCodeGallery ->
                    openGallery()
            }
        }
    }

    var galleryRl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK){
            resimUri = it!!.data!!.data!!

            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, resimUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            photoList.add(bitmap!!)
            val bitmapAdd = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
            photoList.add(bitmapAdd)
            binding.rwPhotosVisitation.adapter?.notifyDataSetChanged()
        }
    }

    private fun openGallery(){
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        galleryRl.launch(intentGallery)
    }

    fun initLm() {
        val lm = LinearLayoutManager(requireContext())
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
        photoList.add(bitmap)
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotosVisitation.layoutManager = lm
        binding.rwPhotosVisitation.adapter = PhotoAdapter(
            requireContext(),
            photoList,
            ::itemClick,
            ::itemDeleteClick,
            ::itemAddPhotoClick
        )
    }

    fun itemClick(position: Int) {
        if (position == photoList.size-1){
        if(photoList.size>0){
            photoList.remove(photoList[photoList.size-1])
        }
            checkGalleryPermissions()
        }
    }

    fun itemDeleteClick(position: Int) {
        photoList.removeAt(position)
        binding.rwPhotosVisitation.adapter!!.notifyDataSetChanged()

    }

    fun itemAddPhotoClick(position: Int) {
        //galeriIzinKontrol()
    }
}