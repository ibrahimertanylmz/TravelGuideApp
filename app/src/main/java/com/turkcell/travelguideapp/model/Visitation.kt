package com.turkcell.travelguideapp.model

import kotlin.properties.Delegates

class Visitation(val date: String, val description: String, val idFromPlace: Int) {
    var id by Delegates.notNull<Int>()
}