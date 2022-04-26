package com.turkcell.travelguideapp.bll

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.model.Visitation

object PlaceLogic {
    private var listAllPlaces = ArrayList<Place>()
    var isAddPlaceFragment: Boolean = false

    private fun fillPlacesList(dbOperation: TravelGuideOperation) {
        listAllPlaces.clear()
        listAllPlaces = dbOperation.returnAllPlaces()
    }

    //RELEASE'DEN ÖNCE BUNU VE KULLANILDIĞI YERDEN SİL
    fun debugTmpFillList(dbOperation: TravelGuideOperation, context: Context) {
        val tmpList = dbOperation.returnAllPlaces()
        if (tmpList.size == 0) {
            //add place function goes here with couple custom places
            val tmpLatLong = LatLng(15.0, 15.0)

            dbOperation.addPlace(
                Place(
                    "place1",
                    tmpLatLong,
                    "defintion text",
                    "description text",
                    Priority.THREE
                )
            )
            dbOperation.addPlace(
                Place(
                    "place2",
                    tmpLatLong,
                    "defintion text",
                    "description text",
                    Priority.THREE
                )
            )
            dbOperation.addPlace(
                Place(
                    "place3",
                    tmpLatLong,
                    "defintion text",
                    "description text",
                    Priority.THREE,
                    ""
                )
            )
            dbOperation.addPlace(
                Place(
                    "place4",
                    tmpLatLong,
                    "defintion text",
                    "description text",
                    Priority.THREE,
                    "21.21.21"
                )
            )
            dbOperation.addPlace(
                Place(
                    "place5",
                    tmpLatLong,
                    "defintion text",
                    "description text",
                    Priority.THREE,
                    "22.22.22"
                )
            )
        }
    }

    fun returnPlacesToVisit(dbOperation: TravelGuideOperation): ArrayList<Place> {
        fillPlacesList(dbOperation)
        return listAllPlaces.filter {
            it.lastVisitDate.isEmpty()
        } as ArrayList<Place>
    }

    fun returnVisitedPlaces(dbOperation: TravelGuideOperation): ArrayList<Place> {
        fillPlacesList(dbOperation)
        return listAllPlaces.filter {
            it.lastVisitDate.isNotEmpty()
        } as ArrayList<Place>
    }

    fun addPlace(context: Context, place: Place) {
        TravelGuideOperation(context).addPlace(place)
    }

    fun getPlaceFromId(context: Context, id: Int): Place? {
        return TravelGuideOperation(context).getPlaceFromId(id)
    }

    fun getVisitationsOfPlace(placeId: Int, context: Context): ArrayList<Visitation> {
        return TravelGuideOperation(context).getVisitationsByPlaceId(placeId)
    }

    fun getPlaceById(dbOperation: TravelGuideOperation, placeId: Int): Place {
        fillPlacesList(dbOperation)
        var filteredList = listAllPlaces.filter {
            it.id == placeId
        }
        return filteredList[0]
    }
}