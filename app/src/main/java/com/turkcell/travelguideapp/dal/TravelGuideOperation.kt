package com.turkcell.travelguideapp.dal

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
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

    @SuppressLint("Range")
    fun getAllPlaces(): ArrayList<Place>{
        val placeList = ArrayList<Place>()
        var place : Place
        open()
        var cursor : Cursor = getPlaces()
        if(cursor.moveToFirst()){
            do {
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val latitude = cursor.getString(cursor.getColumnIndex("Latitude"))
                val longitude = cursor.getString(cursor.getColumnIndex("Longitude"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                val definition = cursor.getString(cursor.getColumnIndex("Definition"))
                val priority = Priority.valueOf(cursor.getString(cursor.getColumnIndex("Priority")))

                place = Place(name, LatLng(latitude.toDouble(),longitude.toDouble()),definition,description,priority)
                place.id = cursor.getInt(0)
                placeList.add(place)

            }while (cursor.moveToNext())

        }

        close()
        return placeList
    }


    private fun getPlaces() : Cursor {
        val sorgu = "Select * from Place"
        return travelGuideDatabase!!.rawQuery(sorgu, null)
    }

}