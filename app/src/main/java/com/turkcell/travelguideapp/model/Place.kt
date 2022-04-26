package com.turkcell.travelguideapp.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class Place(
    var name: String,
    var location: LatLng,
    var definition: String,
    var description: String,
    var priority: Priority
){
    var id by Delegates.notNull<Int>()
    var imageList = ArrayList<Bitmap>()
    var visitationList = ArrayList<Visitation>()
    var lastVisitDate: String? = null
}