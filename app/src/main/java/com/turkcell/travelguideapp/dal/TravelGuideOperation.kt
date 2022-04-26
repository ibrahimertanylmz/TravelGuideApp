package com.turkcell.travelguideapp.dal

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority

class TravelGuideOperation(context: Context) {
    private var travelGuideDatabase: SQLiteDatabase? = null
    private var dbOpenHelper: DatabaseOpenHelper

    init {
        dbOpenHelper = DatabaseOpenHelper(context, "TravelGuideDb", null, 1)
    }

    private fun open() {
        travelGuideDatabase = dbOpenHelper.writableDatabase
    }

    private fun close() {
        if (travelGuideDatabase != null && travelGuideDatabase!!.isOpen) {
            travelGuideDatabase!!.close()
        }
    }


    fun insertPlace(place: Place) {

        val cv = ContentValues()
        cv.put("Name", place.name)
        cv.put("Latitude", place.location.latitude)
        cv.put("Longitude", place.location.longitude)
        cv.put("Description", place.description)
        cv.put("Definition", place.definition)
        cv.put("Priority", place.priority.name) // name olarak mı eklenecek bunu mutlaka sor

        open()
        travelGuideDatabase!!.insert("Place", null, cv)
        close()

    }

    @SuppressLint("Range")
    fun getPlaceFromId(id: Int): Place? {
        var place: Place? = null
        open()
        val sql = "SELECT * FROM Place WHERE id=?"
        val c = travelGuideDatabase!!.rawQuery(sql, arrayOf(id.toString()))

        if (c.moveToFirst()) {
            place!!.id = c.getInt(c.getColumnIndex("id"))

            val latitude = c.getString(c.getColumnIndex("Latitude"))
            val longitude = c.getString(c.getColumnIndex("Longitude"))

            place!!.location = LatLng(latitude.toDouble(), longitude.toDouble())
            place!!.definition = c.getString(c.getColumnIndex("definition"))
            place!!.description = c.getString(c.getColumnIndex("description"))
            place!!.priority = Priority.valueOf(c.getString(c.getColumnIndex("priorty")))


        }
        close()

        return place
    }


}