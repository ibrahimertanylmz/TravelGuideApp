package com.turkcell.travelguideapp.view.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.turkcell.travelguideapp.R
import com.turkcell.travelguideapp.bll.ImageLogic
import com.turkcell.travelguideapp.bll.PlaceLogic
import com.turkcell.travelguideapp.bll.VisitationLogic
import com.turkcell.travelguideapp.databinding.ActivityAddVisitationBinding
import com.turkcell.travelguideapp.databinding.PopUpBinding
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Visitation
import com.turkcell.travelguideapp.view.adapter.PhotoAdapter
import java.io.File
import java.io.IOException
import java.util.*

class AddVisitationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddVisitationBinding

    private var placeId: Int = -1
    private lateinit var currentPlace: Place

    private var photoList = ArrayList<Bitmap>()
    lateinit var resimYolu: String
    val requestCodeGallery = 1001
    val requestCodeCamera = 1002
    lateinit var resimUri: Uri

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaults()
        initializeViews()
        initializeEvents()
    }

    private fun setDefaults() {
        placeId = intent.getIntExtra("place_id_for_add_visitation", -1)
        currentPlace = PlaceLogic.getPlaceById(dbOperation, placeId)
    }

    private fun initializeViews() {
        binding.includeTop.tvTopBarTitle.text = PlaceLogic.tmpPlace.name
        binding.includeBottom.llBottom.visibility = View.INVISIBLE
        binding.includeBottom.btnWide.visibility = View.VISIBLE
        binding.includeBottom.tabLayout.visibility = View.INVISIBLE

        initLm()
    }

    private fun initializeEvents() {
        binding.includeTop.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.edtVisitDate.setOnClickListener {
            setDate()
        }

        binding.includeBottom.btnWide.setOnClickListener {
            if (binding.edtVisitDate.text.toString() != "" && binding.edtVisitDesc.text.toString() != "") {
                val tmpVisitation = Visitation(
                    binding.edtVisitDate.text.toString(),
                    binding.edtVisitDesc.text.toString(),
                    placeId
                )
                VisitationLogic.addVisitation(dbOperation, tmpVisitation)
                photoList.removeLast()
                photoList.forEach {
                    ImageLogic.addImage(this, it, placeId)
                }
                Toast.makeText(
                    this,
                    "Ziyaret Başarıyla Eklendi!",
                    Toast.LENGTH_SHORT
                ).show()
                PlaceLogic.updateLastVisitDate(this, placeId, binding.edtVisitDate.text.toString() )
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.please_fill_all_the_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun setDate() {
        val cal: Calendar = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        var year = cal.get(Calendar.YEAR)

        val dpd =
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    binding.edtVisitDate.setText("${day}.${month + 1}.${year}")
                },
                year,
                month,
                day
            )
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun initLm() {
        val lm = LinearLayoutManager(this)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
        photoList.add(bitmap)
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotosVisitation.layoutManager = lm
        binding.rwPhotosVisitation.adapter = PhotoAdapter(
            this,
            photoList,
            ::itemClick,
            ::itemDeleteClick,
            ::itemAddPhotoClick
        )
    }

    var cameraRl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
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
            binding.rwPhotosVisitation.adapter?.notifyDataSetChanged()
            binding.rwPhotosVisitation.getLayoutManager()?.scrollToPosition(photoList.size-1);
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
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

    var galleryRl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            resimUri = it!!.data!!.data!!

            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resimUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //if (photoList.size== 1) {photoList.removeAt(0) }
            photoList.add(bitmap!!)
            val bitmapAdd = BitmapFactory.decodeResource(resources, R.drawable.add_photo)
            photoList.add(bitmapAdd)
            binding.rwPhotosVisitation.adapter?.notifyDataSetChanged()
            binding.rwPhotosVisitation.getLayoutManager()?.scrollToPosition(photoList.size-1);
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
        resimUri = FileProvider.getUriForFile(this, this.packageName, dosya)
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
        binding.rwPhotosVisitation.adapter!!.notifyDataSetChanged()
    }

    private fun itemClick(position: Int) {
        if(position == (photoList.size -1) &&(photoList.size<10)){
            showPopUp(position)
        }else{
            //Toast.makeText(this, "10 taneden fazla fotoğraf eklenemez", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun itemDeleteClick(position: Int) {
        photoList.removeAt(position)
        binding.rwPhotosVisitation.adapter!!.notifyDataSetChanged()
    }

    private fun itemAddPhotoClick(position: Int) {
    }
}