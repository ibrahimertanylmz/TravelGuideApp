package com.turkcell.travelguideapp.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class Place(
    val name: String,
    val location: LatLng,
    val definition: String,
    val description: String,
    var priority: Priority
) {
    var id by Delegates.notNull<Int>()
    var imageList = ArrayList<Bitmap>()
    var visitationList = ArrayList<Visitation>()
    var lastVisitDate: String? = null
}