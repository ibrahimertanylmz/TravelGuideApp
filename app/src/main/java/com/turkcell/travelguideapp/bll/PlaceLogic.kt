package com.turkcell.travelguideapp.bll

import android.content.Context
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Visitation

object PlaceLogic {
    private var listAllPlaces = ArrayList<Place>()

    private fun fillPlacesList(dbOperation: TravelGuideOperation) {
        listAllPlaces.clear()
        listAllPlaces = dbOperation.returnAllPlaces()
    }

    private fun debugTmpFillList(dbOperation: TravelGuideOperation) {
        val tmpList = dbOperation.returnAllPlaces()
        if (tmpList.size == 0) {
            //add place function goes here with couple custom places
        }
    }

    fun returnPlacesToVisit(dbOperation: TravelGuideOperation): ArrayList<Place> {
        fillPlacesList(dbOperation)
        return listAllPlaces.filter { place -> place.lastVisitDate == null } as ArrayList<Place>
    }

    fun returnVisitedPlaces(dbOperation: TravelGuideOperation): ArrayList<Place> {
        fillPlacesList(dbOperation)
        return listAllPlaces.filter { place -> place.lastVisitDate != null } as ArrayList<Place>
    }
    
    val listPlaces = ArrayList<Place>()

    fun addPlace(context: Context,place: Place){
        TravelGuideOperation(context).insertPlace(place)
    }
    
    //fonksiyon adı refactor edilecek
    fun getPlaceId(context:Context,id:Int):Place?{
       return TravelGuideOperation(context).getPlaceFromId(id)

    /*fun getPlaceList(context: Context): ArrayList<Place>{
        return TravelGuideOperation(context).getAllPlaces()
    }*/

    fun getVisitationsOfPlace(placeId: Int, context: Context): ArrayList<Visitation>{
        return TravelGuideOperation(context).getVisitationsByPlaceId(placeId)
    }

    fun getPlaceById(placeId: Int): Place {
        return listPlaces[placeId]
    }
}