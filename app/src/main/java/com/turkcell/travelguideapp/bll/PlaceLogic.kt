package com.turkcell.travelguideapp.bll

import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.model.Place

object PlaceLogic {
    private var listAllPlaces = ArrayList<Place>()

    private fun fillPlacesList(dbOperation: TravelGuideOperation) {
        listAllPlaces.clear()
        listAllPlaces = dbOperation.returnPlaces()
    }

    fun returnPlacesToVisit(dbOperation: TravelGuideOperation): ArrayList<Place> {
        fillPlacesList(dbOperation)
        return listAllPlaces.filter { place -> place.lastVisitDate == null } as ArrayList<Place>
    }

    fun returnVisitedPlaces(dbOperation: TravelGuideOperation): ArrayList<Place> {
        fillPlacesList(dbOperation)
        return listAllPlaces.filter { place -> place.lastVisitDate != null } as ArrayList<Place>
    }
}