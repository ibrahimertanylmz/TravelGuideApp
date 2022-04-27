package com.turkcell.travelguideapp.view.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    val requestCodeCamera = 0
    val requestCodeGallery = 1
    lateinit var resimYolu : String
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

        binding.imageAddPhoto.setOnClickListener {
            binding.clAddPhoto.visibility = View.GONE
            itemAddPhotoClick(0)
        }


        return binding.root
    }



    @RequiresApi(Build.VERSION_CODES.M)
    fun galeriIzinKontrol(){
        val requestList = ArrayList<String>()

        var izinDurum = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        if(!izinDurum){
            requestList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        izinDurum = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        if(!izinDurum){
            requestList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if(requestList.size == 0){
            galeriAc()
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)){
                    //reddedildi
                }else if(ContextCompat.checkSelfPermission(requireContext(), permission)== PackageManager.PERMISSION_GRANTED){
                    //onaylandı
                }else{
                    //tekrar gosterme seçildi
                    tekrarGosterme = true
                    break
                }
            }

            if (tekrarGosterme){
                val adb = AlertDialog.Builder(requireContext())
                adb.setTitle("İzin Gerekli")
                    .setMessage("Ayarlara giderek tüm izinleri onaylayınız")
                    .setPositiveButton("Ayarlar", { dialog, which -> ayarlarAc() })
                    .setNegativeButton("Vazgeç", null)
                    .show()
            }

        }else{

            when(requestCode){
                requestCodeCamera ->
                    kameraAc()
                requestCodeGallery ->
                    galeriAc()
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
            binding.rwPhotosVisitation.adapter?.notifyDataSetChanged()
        }
    }

    var cameraRl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK){

            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, resimUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //ImageLogic.addImage(requireContext(),bitmap!!,placeId)
            photoList.add(bitmap!!)
            binding.rwPhotosVisitation.adapter?.notifyDataSetChanged()
        }
    }

    private fun ayarlarAc() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)

    }

    private fun kameraAc() {
        /*val intent  = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraRl.launch(intent)*/
        val intent  = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val dosya = resimDosyasiOlustur()
        resimUri = FileProvider.getUriForFile(requireContext(),requireActivity().packageName, dosya)
        //*******
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resimUri)
        cameraRl.launch(intent)

    }

    private fun galeriAc(){
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        galleryRl.launch(intentGallery)
    }



    @Throws(IOException::class)
    fun resimDosyasiOlustur(): File {
        val dir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("resim", "jpg", dir).apply {
            resimYolu = absolutePath
        }
    }

    fun initLm() {
        val lm = LinearLayoutManager(requireContext())

        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rwPhotosVisitation.layoutManager = lm
        binding.rwPhotosVisitation.adapter = PhotoAdapter(
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
        galeriIzinKontrol()
    }
}