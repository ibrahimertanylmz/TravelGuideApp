package com.turkcell.travelguideapp.bll

import android.content.Context
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Visitation

object PlaceLogic {
    val listPlaces = ArrayList<Place>()

    fun getPlaceList(context: Context): ArrayList<Place>{
        return TravelGuideOperation(context).getAllPlaces()
    }

    fun getVisitationsOfPlace(placeId: Int, context: Context): ArrayList<Visitation>{
        return TravelGuideOperation(context).getVisitationsByPlaceId(placeId)
    }

    fun getPlaceById(placeId: Int): Place {
        return listPlaces[placeId]
    }
}