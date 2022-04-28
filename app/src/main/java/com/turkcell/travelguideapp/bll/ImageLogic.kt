package com.turkcell.travelguideapp.bll

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.turkcell.travelguideapp.dal.TravelGuideOperation

object ImageLogic {

    var imageList = ArrayList<Bitmap>()

    fun getImagesByPlaceId(context: Context, placeId: Int): ArrayList<Bitmap>{
        return TravelGuideOperation(context).getImagesByPlaceId(placeId)
    }

    fun addImage(context: Context, bitmap: Bitmap, placeId: Int){
        TravelGuideOperation(context).addImage(bitmap,placeId)
    }

    fun showAlert(context: Context){
        val adb = AlertDialog.Builder(context)
        adb.setTitle("İzin Gerekli")
            .setMessage("Ayarlara giderek tüm izinleri onaylayınız")
            .setPositiveButton("Ayarlar", { dialog, which -> openSettings(context) })
            .setNegativeButton("Vazgeç", null)
            .show()
    }

    private fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun checkCameraPermissions(context: Context): ArrayList<String>{
        val requestList = java.util.ArrayList<String>()

        var izinDurum = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        if(!izinDurum){
            requestList.add(Manifest.permission.CAMERA)
        }

        izinDurum = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        if(!izinDurum){
            requestList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        izinDurum = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        if(!izinDurum){
            requestList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        return requestList
    }

    fun checkGalleryPermissions(context: Context): ArrayList<String>{
        val requestList = java.util.ArrayList<String>()

        var izinDurum = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        if(!izinDurum){
            requestList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        izinDurum = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        if(!izinDurum){
            requestList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        return requestList
    }

}