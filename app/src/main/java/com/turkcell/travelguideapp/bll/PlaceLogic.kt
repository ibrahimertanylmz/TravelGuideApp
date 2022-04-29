package com.turkcell.travelguideapp.bll

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.model.Visitation

object PlaceLogic {
    private var listAllPlaces = ArrayList<Place>()

    lateinit var tmpPlace: Place
    var tmpPlaceId: Int = -1


    private fun fillPlacesList(dbOperation: TravelGuideOperation) {
        listAllPlaces.clear()
        listAllPlaces = dbOperation.returnAllPlaces()
    }

    fun debugTmpFillList(dbOperation: TravelGuideOperation) {
        val tmpList = dbOperation.returnAllPlaces()
        if (tmpList.size == 0) {
            val tmpLatLong = LatLng(15.0, 15.0)

            dbOperation.addPlace(
                Place(
                    "Place1",
                    tmpLatLong,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    "Phasellus sed nisl sit amet dolor sagittis eleifend vel efficitur ex. Nullam ut justo ex. Morbi ullamcorper libero ac malesuada semper.",
                    Priority.ONE
                )
            )
            dbOperation.addPlace(
                Place(
                    "Place2",
                    tmpLatLong,
                    "Cras justo justo, posuere sed eros suscipit, rhoncus suscipit risus.",
                    "Sed mollis, augue vitae iaculis feugiat, neque metus egestas nibh, sit amet tempor nulla metus sed diam.",
                    Priority.TWO
                )
            )
            dbOperation.addPlace(
                Place(
                    "place3",
                    tmpLatLong,
                    "Vivamus sed ullamcorper dui.",
                    "Donec in turpis vel sem luctus facilisis non vitae justo. Vestibulum quis velit ac enim elementum eleifend in vitae ex.",
                    Priority.THREE,
                    ""
                )
            )
            dbOperation.addPlace(
                Place(
                    "Place4",
                    tmpLatLong,
                    "Cras at nulla sem. Mauris in lobortis dolor.",
                    "Vestibulum sit amet elit ultrices, mollis velit eget, rhoncus sapien.",
                    Priority.TWO,
                    "21.02.2022"
                )
            )
            dbOperation.addPlace(
                Place(
                    "Place5",
                    tmpLatLong,
                    "Nullam ante arcu, venenatis non porttitor vel, pellentesque rhoncus urna.",
                    "Nam varius diam a nisi imperdiet fermentum sit amet sit amet odio. Vestibulum gravida aliquam ipsum et vulputate.",
                    Priority.THREE,
                    "22.02.2022"
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

    fun getPlaceFromId(context: Context, id: Int): Place {
        return TravelGuideOperation(context).getPlaceFromId(id)
    }

    fun updateLastVisitDate(context: Context, id: Int, lastVisitDate: String){
        TravelGuideOperation(context).updatePlace(id,lastVisitDate)
    }

    fun getVisitationsOfPlace(placeId: Int, context: Context): ArrayList<Visitation> {
        return TravelGuideOperation(context).getVisitationsByPlaceId(placeId)
    }

    fun getPlaceById(dbOperation: TravelGuideOperation, placeId: Int): Place {
        fillPlacesList(dbOperation)
        val filteredList = listAllPlaces.filter {
            it.id == placeId
        }
        return filteredList[0]
    }

    fun getImagesOfPlace(context: Context){
        listAllPlaces.forEach {
            it.imageList = ImageLogic.getImagesByPlaceId(context,it.id)
        }
    }
}