package com.turkcell.travelguideapp.model

import com.google.android.gms.maps.model.LatLng
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class Place(
    val name: String,
    val location: LatLng,
    val definition: String = "",
    val description: String,
    val priority: Priority,
    var lastVisitDate: String = ""
){
    var id by Delegates.notNull<Int>()
    var imageList = ArrayList<Any>()
    var visitationList = ArrayList<Visitation>()
}