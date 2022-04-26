package com.turkcell.travelguideapp.model

import kotlin.properties.Delegates

class Visitation(val date: String, val description: String) {
    var id by Delegates.notNull<Int>()
}