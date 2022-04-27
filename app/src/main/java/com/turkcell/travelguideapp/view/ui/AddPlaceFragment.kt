package com.turkcell.travelguideapp.view.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.ImageLogic
import com.turkcell.travelguideapp.bll.MapLogic
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.databinding.FragmentAddPlaceBinding
import com.turkcell.travelguideapp.databinding.PopUpBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter
import java.io.File
import java.io.IOException

class AddPlaceFragment : Fragment() {
    private lateinit var binding: FragmentAddPlaceBinding
    //private var placeId = -1

    private var photoList = ArrayList<Bitmap>()
    //lateinit var fotoğrafEkleView: ImageView

    private var priority: Priority = Priority.THREE
    private var getLocation: LatLng? = null

    private var getLatitude: Double? = null
    private var getLongitude: Double? = null
    val requestCodeGallery = 1001
    val requestCodeCamera = 1002
    lateinit var resimUri: Uri
    lateinit var resimYolu : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddPlaceBinding.inflate(inflater)

        (requireActivity() as MainActivity).binding.includeTop.tvTopBarTitle.text =
            getString(R.string.Add_Place)
        (requireActivity() as MainActivity).changeBackButtonVisibility(true)
        (requireActivity() as MainActivity).changeTabLayoutVisibility(false)
        (requireActivity() as MainActivity).changeViewPagerVisibility(false)
        (requireActivity() as MainActivity).binding.includeTop.btnBack.setOnClickListener {
            val action = AddPlaceFragmentDirections.actionAddPlaceFragmentToPlacesToVisitFragment()
            findNavController().navigate(action)
        }


        spinnerListOperations()

        initLm()

        binding.btnAddLocation.setOnClickListener {
            btnAddLocationOnClick()
        }

        (activity as MainActivity).binding.includeBottom.btnWide.setOnClickListener {
            btnSaveOnClick()
        }

        return binding.root
    }

    private fun btnSaveOnClick() {
        lateinit var p: Place
        //Definition and photos are optional
        if (binding.edtPlaceName.text.toString().isNotEmpty()) {
            if (binding.edtDescription.text.toString().isNotEmpty()) {
                if (MapLogic.tmpMap.lat != 0.0 && MapLogic.tmpMap.long != 0.0) {
                    p = Place(
                        binding.edtPlaceName.text.toString(),
                        LatLng(MapLogic.tmpMap.lat, MapLogic.tmpMap.long),
                        binding.edtDefinition.text.toString(),
                        binding.edtDescription.text.toString(),
                        priority
                    )
                    PlaceLogic.addPlace(requireContext(), p)
                    Toast.makeText(requireContext(), getString(R.string.place_added_successfuly), Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val adb = AlertDialog.Builder(requireContext())
                    adb.setTitle(getString(R.string.LOCATION))
                    adb.setMessage(getString(R.string.add_place_fragment_choose_location_confirmation))
                    adb.setPositiveButton(getString(R.string.Confirm)) { _, _ ->
                        (activity as MainActivity).openMapsActivityFromAddPlaceFragment()
                    }
                    adb.setNegativeButton(getString(R.string.No), null)
                    adb.show()
                }
            } else {
                binding.edtDescription.error = getString(R.string.This_field_cannot_be_empty)
            }
        } else {
            binding.edtPlaceName.error = getString(R.string.This_field_cannot_be_empty)
        }
    }

    private fun btnAddLocationOnClick() {
        (activity as MainActivity).openMapsActivityFromAddPlaceFragment()
    }


    private fun spinnerListOperations() {
        val liste = arrayListOf("Öncelik Seç", "Öncelik 1", "Öncelik 2", "Öncelik 3")

        val adap =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, liste)
        binding.spinnerPriority.adapter = adap

        binding.spinnerPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    when (p2) {
                        1 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_green)
                            priority = Priority.ONE
                        }
                        2 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_blue)
                            priority = Priority.TWO
                        }
                        3 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_gray)
                            priority = Priority.THREE
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
            }
    }

    private fun initLm() {
        val lm = LinearLayoutManager(requireContext())
        val bitmapAdd = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
        photoList.add(bitmapAdd)
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotos.layoutManager = lm
        binding.rwPhotos.adapter =
            PhotoAdapter(
                requireContext(),
                photoList,
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
            binding.rwPhotos.adapter?.notifyDataSetChanged()
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
                    openCamera()
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
            binding.rwPhotos.adapter?.notifyDataSetChanged()
        }
    }

    private fun openGallery(){
        if (photoList.size == 1){
            photoList.removeAt(0)
        }
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
            setAddPhotoImage(position)
            openCamera()
        }else{
            requestPermissions(requestList.toTypedArray(), requestCodeCamera)
        }
    }

    private fun checkGalleryPermissions(position: Int){
        val requestList = ImageLogic.checkGalleryPermissions(requireContext())
        if(requestList.size == 0){
            setAddPhotoImage(position)
            openGallery()
        }else{
            requestPermissions(requestList.toTypedArray(), requestCodeGallery)
        }
    }

    private fun openCamera() {
        if (photoList.size == 1){
            photoList.removeAt(0)
        }
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
        binding.rwPhotos.adapter!!.notifyDataSetChanged()
    }

    fun itemClick(position: Int) {
       showPopUp(position)
    }

    fun itemDeleteClick(position: Int) {
        photoList.removeAt(position)
        binding.rwPhotos.adapter!!.notifyDataSetChanged()

    }
    private fun itemAddPhotoClick(position: Int) {
        //open gallery and select photo
    }

}