package com.turkcell.travelguideapp.bll

import android.content.Context
import android.graphics.Bitmap
import com.turkcell.travelguideapp.dal.TravelGuideOperation

object ImageLogic {

    fun getImagesByPlaceId(context: Context, placeId: Int): ArrayList<Bitmap>{
        return TravelGuideOperation(context).getImagesByPlaceId(placeId)
    }

    fun addImage(context: Context, bitmap: Bitmap, placeId: Int){
        TravelGuideOperation(context).addImage(bitmap,placeId)
    }

}