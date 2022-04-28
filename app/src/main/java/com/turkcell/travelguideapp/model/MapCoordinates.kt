package com.turkcell.travelguideapp.model

class MapCoordinates(var lat: Double = 0.0, var long: Double = 0.0) {
    var latlongString: String = "$lat $long"
}