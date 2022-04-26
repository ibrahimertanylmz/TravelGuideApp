package com.turkcell.travelguideapp.bll

import android.content.Context
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.model.Place

object PlaceLogic {
    val listPlaces = ArrayList<Place>()

    fun addPlace(context: Context,place: Place){
        TravelGuideOperation(context).insertPlace(place)
    }

    fun getPlaceId(context:Context,id:Int):Place?{
       return TravelGuideOperation(context).getPlaceFromId(id)
    }
}