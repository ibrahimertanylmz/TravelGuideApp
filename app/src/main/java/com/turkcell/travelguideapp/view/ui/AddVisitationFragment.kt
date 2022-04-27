package com.turkcell.travelguideapp.view.ui

import android.Manifest
import android.content.DialogInterface
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
import android.widget.Button
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
import com.turkcell.travelguideapp.databinding.FragmentAddVisitationBinding
import com.turkcell.travelguideapp.databinding.PopUpBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter
import java.io.File
import java.io.IOException
import java.util.*

class AddVisitationFragment : Fragment() {
    private lateinit var binding: FragmentAddVisitationBinding
    private var placeId: Int = -1
    private lateinit var currentPlace: Place
  
    private var photoList = ArrayList<Bitmap>()
    lateinit var resimYolu : String
    val requestCodeGallery = 1001
    val requestCodeCamera = 1002
    lateinit var resimUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddVisitationBinding.inflate(inflater)

        setDefaults()
        initializeViews()
        initializeEvents()

        return binding.root
    }

    private fun setDefaults() {
        placeId = requireArguments().getInt("place_id_for_add_visitation")
        currentPlace = PlaceLogic.getPlaceById(dbOperation, placeId)
    }

    private fun initializeViews() {
        (requireActivity() as MainActivity).changeMainActivityHuds(
            setBackButtonVisible = true,
            setTabLayoutVisibleAndBtnWideInvisible = false,
            setViewPagerVisible = false,
            titleString = currentPlace.name
        )

        //ne işe yaradığına bak??
        //initLm()
    }

    private fun initializeEvents() {
        (requireActivity() as MainActivity).binding.includeTop.btnBack.setOnClickListener {
            val action =
                AddVisitationFragmentDirections.actionAddVisitationFragmentToPlaceDetailsFragment(
                    placeId
                )
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
    }

    private fun initLm() {
        val lm = LinearLayoutManager(requireContext())
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
        photoList.add(bitmap)
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotosVisitation.layoutManager = lm
        binding.rwPhotosVisitation.adapter = PhotoAdapter(
            requireContext(),
            currentPlace.imageList,
            ::itemClick,
            ::itemDeleteClick,
            ::itemAddPhotoClick
        )
    }

    var cameraRl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK){
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, resimUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //if (photoList.size== 1) {photoList.removeAt(0) }
            photoList.add(bitmap!!)
            val bitmapAdd = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
            photoList.add(bitmapAdd)
            binding.rwPhotosVisitation.adapter?.notifyDataSetChanged()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allAccepted = true
        for( gr in grantResults){
            if (gr != PackageManager.PERMISSION_GRANTED){
                allAccepted = false
                break
            }
        }
        if(!allAccepted){
            var neverShowAgain = false
            for(permission in permissions){
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)){ }
                else if(ContextCompat.checkSelfPermission(requireContext(), permission)== PackageManager.PERMISSION_GRANTED){ }
                else{
                    neverShowAgain = true
                    break
                }
            }
            if (neverShowAgain){
                ImageLogic.showAlert(requireContext())
            }
        }else{
            when(requestCode){
                requestCodeCamera ->
                    openCamera(0)
                requestCodeGallery ->
                    openGallery(0)
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
            //if (photoList.size== 1) {photoList.removeAt(0) }
            photoList.add(bitmap!!)
            val bitmapAdd = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
            photoList.add(bitmapAdd)
            binding.rwPhotosVisitation.adapter?.notifyDataSetChanged()
        }
    }

    private fun openGallery(position: Int){
        setAddPhotoImage(position)
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        galleryRl.launch(intentGallery)
    }

    private fun showPopUp(position: Int){
        var bindingTwo = PopUpBinding.inflate(layoutInflater)
        val v = bindingTwo.root
        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        val popUyari = PopupWindow(v, v.measuredWidth, v.measuredHeight, true)
        popUyari.showAtLocation(v, Gravity.CENTER, 0, 0)

        bindingTwo.btnCamera.setOnClickListener {
            popUyari.dismiss()
            checkCameraPermissions(position)
        }
        bindingTwo.btnGallery.setOnClickListener {
            popUyari.dismiss()
            checkGalleryPermissions(position)
        }
    }

    private fun checkCameraPermissions(position: Int){
        val requestList = ImageLogic.checkCameraPermissions(requireContext())
        if(requestList.size == 0){
            openCamera(position)
        }else{
            requestPermissions(requestList.toTypedArray(), requestCodeCamera)
        }
    }

    private fun checkGalleryPermissions(position: Int){
        val requestList = ImageLogic.checkGalleryPermissions(requireContext())
        if(requestList.size == 0){
            openGallery(position)
        }else{
            requestPermissions(requestList.toTypedArray(), requestCodeGallery)
        }
    }

    private fun openCamera(position: Int) {
        setAddPhotoImage(position)
        val intent  = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val dosya = createImageFile()
        resimUri = FileProvider.getUriForFile(requireContext(), requireActivity().packageName, dosya)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resimUri)
        cameraRl.launch(intent)
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val dir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("resim", "jpg", dir).apply {
            resimYolu = absolutePath
        }
    }

    fun setAddPhotoImage(position: Int){
        if (position == photoList.size-1){
            if(photoList.size>0){
                photoList.remove(photoList[photoList.size-1])
            }
        }
        binding.rwPhotosVisitation.adapter!!.notifyDataSetChanged()
    }

    fun itemClick(position: Int) {
        showPopUp(position)
    }

    fun itemDeleteClick(position: Int) {
        photoList.removeAt(position)
        binding.rwPhotosVisitation.adapter!!.notifyDataSetChanged()
    }

    fun itemAddPhotoClick(position: Int) {

    }
}