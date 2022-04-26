package com.turkcell.travelguideapp.bll

import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority

object PlaceLogic {
    private val listAllPlaces = ArrayList<Place>()

    fun tmpFillPlacesList() {
        listAllPlaces.add(Place("place1", LatLng(10.0, 10.0), "def text", "desc text", Priority.ONE).apply { this.lastVisitDate = "22.22.22" })
        listAllPlaces.add(Place("place2", LatLng(10.0, 10.0), "def text", "desc text", Priority.ONE).apply { this.lastVisitDate = "22.22.22" })
        listAllPlaces.add(Place("place3", LatLng(10.0, 10.0), "def text", "desc text", Priority.ONE))
        listAllPlaces.add(Place("place4", LatLng(10.0, 10.0), "def text", "desc text", Priority.ONE))
        listAllPlaces.add(Place("place5", LatLng(10.0, 10.0), "def text", "desc text", Priority.ONE))
    }

    fun returnPlacesToVisit(): ArrayList<Place> {
        return listAllPlaces.filter { place -> place.lastVisitDate == null } as ArrayList<Place>
    }

    fun returnVisitedPlaces(): ArrayList<Place> {
        return listAllPlaces.filter { place -> place.lastVisitDate != null } as ArrayList<Place>
    }
}