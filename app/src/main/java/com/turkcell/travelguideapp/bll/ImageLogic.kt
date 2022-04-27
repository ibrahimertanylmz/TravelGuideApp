package com.turkcell.travelguideapp.bll

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.turkcell.travelguideapp.dal.TravelGuideOperation

object ImageLogic {

    fun getImagesByPlaceId(context: Context, placeId: Int): ArrayList<Bitmap>{
        return TravelGuideOperation(context).getImagesByPlaceId(placeId)
    }

    fun addImage(context: Context, bitmap: Bitmap, placeId: Int){
        TravelGuideOperation(context).addImage(bitmap,placeId)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun galeriIzinKontrol(context: Context): ArrayList<String>{
        val requestList = ArrayList<String>()

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

    fun showAlert(context: Context){
        val adb = AlertDialog.Builder(context)
        adb.setTitle("İzin Gerekli")
            .setMessage("Ayarlara giderek tüm izinleri onaylayınız")
            .setPositiveButton("Ayarlar", { dialog, which -> ayarlarAc(context) })
            .setNegativeButton("Vazgeç", null)
            .show()
    }

    private fun ayarlarAc(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

}