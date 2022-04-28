package com.turkcell.travelguideapp.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.ImageLogic
import com.turkcell.travelguideapp.bll.MapLogic
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.databinding.ActivityAddBinding
import com.turkcell.travelguideapp.databinding.PopUpBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter
import java.io.File
import java.io.IOException

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    private var defaultPriority: Priority = Priority.THREE

    private var photoList = ArrayList<Bitmap>()
    private val requestCodeGallery = 1001
    private val requestCodeCamera = 1002
    private lateinit var resimUri: Uri
    private lateinit var resimYolu: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        initializeEvents()

        initLm()
    }

    private fun initializeViews() {
        binding.includeTop.tvTopBarTitle.text = getString(R.string.Add_Place)
        binding.includeBottom.tabLayout.visibility = View.INVISIBLE
        binding.includeBottom.llBottom.visibility = View.INVISIBLE
        binding.includeBottom.btnWide.visibility = View.VISIBLE

        spinnerListOperations()
    }

    private fun initializeEvents() {
        binding.includeTop.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnAddLocation.setOnClickListener {
            Toast.makeText(this, "Feature not implemented!", Toast.LENGTH_SHORT).show()
        }

        binding.includeBottom.btnWide.setOnClickListener {
            addCurrentPlaceToDb()
        }
    }

    private fun addCurrentPlaceToDb() {
        lateinit var p: Place
        if (binding.edtPlaceName.text.toString().isNotEmpty()) {
            if (binding.edtDescription.text.toString().isNotEmpty()) {
                if (MapLogic.tmpMap.lat != 0.0 && MapLogic.tmpMap.long != 0.0) {
                    p = Place(
                        binding.edtPlaceName.text.toString(),
                        LatLng(MapLogic.tmpMap.lat, MapLogic.tmpMap.long),
                        binding.edtDefinition.text.toString(),
                        binding.edtDescription.text.toString(),
                        defaultPriority,
                        ""
                    )
                    PlaceLogic.addPlace(this, p)
                    addImagesOfPlace()
                    Toast.makeText(
                        this, getString(R.string.place_added_successfuly), Toast.LENGTH_SHORT
                    ).show()
                    this.finish()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    val adb = AlertDialog.Builder(this)
                    adb.setTitle(getString(R.string.LOCATION))
                    adb.setMessage(getString(R.string.add_place_fragment_choose_location_confirmation))
                    adb.setPositiveButton(getString(R.string.Confirm)) { _, _ ->
                        Toast.makeText(this, "Feature not implemented", Toast.LENGTH_SHORT).show()
                        //(activity as MainActivity).openMapsActivityFromAddPlaceFragment()
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

    private fun addImagesOfPlace(){
        val placeId = PlaceLogic.returnPlacesToVisit(dbOperation).last().id
        photoList.removeLast()
        photoList.forEach {
            ImageLogic.addImage(this,it,placeId)
        }
    }

    private fun spinnerListOperations() {
        val list = arrayListOf(
            getString(R.string.Choose_Priority), getString(R.string.Priority_1), getString(
                R.string.Priority_2
            ), getString(R.string.Priority_3)
        )

        val adapt =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        binding.spinnerPriority.adapter = adapt

        binding.spinnerPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    when (p2) {
                        1 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_green)
                            defaultPriority = Priority.ONE
                        }
                        2 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_blue)
                            defaultPriority = Priority.TWO
                        }
                        3 -> {
                            binding.imageCircle.setImageResource(R.drawable.rv_oval_item_gray)
                            defaultPriority = Priority.THREE
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
            }
    }

    private fun initLm() {
        val lm = LinearLayoutManager(this)
        val bitmapAdd = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
        photoList.add(bitmapAdd)
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotos.layoutManager = lm
        binding.rwPhotos.adapter =
            PhotoAdapter(
                this,
                photoList,
                ::itemClick,
                ::itemDeleteClick,
                ::itemAddPhotoClick
            )
    }

    @SuppressLint("NotifyDataSetChanged")
    var cameraRl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            var bitmap: Bitmap? = null
            try {
                bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, resimUri)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allAccepted = true
        for (gr in grantResults) {
            if (gr != PackageManager.PERMISSION_GRANTED) {
                allAccepted = false
                break
            }
        }
        if (!allAccepted) {
            var neverShowAgain = false
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        permission
                    )
                ) {
                } else if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                } else {
                    neverShowAgain = true
                    break
                }
            }
            if (neverShowAgain) {
                ImageLogic.showAlert(this)
            }
        } else {
            when (requestCode) {
                requestCodeCamera ->
                    openCamera(0)
                requestCodeGallery ->
                    openGallery(0)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    var galleryRl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            resimUri = it!!.data!!.data!!

            var bitmap: Bitmap? = null
            try {
                bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, resimUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            photoList.add(bitmap!!)
            val bitmapAdd = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
            photoList.add(bitmapAdd)
            binding.rwPhotos.adapter?.notifyDataSetChanged()
        }
    }

    private fun openGallery(position: Int) {
        setAddPhotoImage(position)
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        galleryRl.launch(intentGallery)
    }

    private fun showPopUp(position: Int) {
        var bindingTwo = PopUpBinding.inflate(layoutInflater)
        val v = bindingTwo.root
        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
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

    private fun checkCameraPermissions(position: Int) {
        val requestList = ImageLogic.checkCameraPermissions(this)
        if (requestList.size == 0) {
            openCamera(position)
        } else {
            requestPermissions(requestList.toTypedArray(), requestCodeCamera)
        }
    }

    private fun checkGalleryPermissions(position: Int) {
        val requestList = ImageLogic.checkGalleryPermissions(this)
        if (requestList.size == 0) {
            openGallery(position)
        } else {
            requestPermissions(requestList.toTypedArray(), requestCodeGallery)
        }
    }

    private fun openCamera(position: Int) {
        setAddPhotoImage(position)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val dosya = createImageFile()
        resimUri =
            FileProvider.getUriForFile(this, this.packageName, dosya)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resimUri)
        cameraRl.launch(intent)
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("resim", "jpg", dir).apply {
            resimYolu = absolutePath
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAddPhotoImage(position: Int) {
        if (position == photoList.size - 1) {
            if (photoList.size > 0) {
                photoList.remove(photoList[photoList.size - 1])
            }
        }
        binding.rwPhotos.adapter!!.notifyDataSetChanged()
    }

    private fun itemClick(position: Int) {
        showPopUp(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun itemDeleteClick(position: Int) {
        photoList.removeAt(position)
        binding.rwPhotos.adapter!!.notifyDataSetChanged()

    }

    private fun itemAddPhotoClick(position: Int) {
        //open gallery and select photo
    }
}