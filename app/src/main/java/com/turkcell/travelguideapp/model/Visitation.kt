package com.turkcell.travelguideapp.model

import android.graphics.Bitmap
import kotlin.properties.Delegates

val visitationList = ArrayList<Visitation>()

class Visitation(val date: String, val description: String) {
    var id by Delegates.notNull<Int>()
    val imageList = ArrayList<Bitmap>()
}