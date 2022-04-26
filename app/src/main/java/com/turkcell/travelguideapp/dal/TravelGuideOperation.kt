package com.turkcell.travelguideapp.dal

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.turkcell.travelguideapp.model.Place
import com.turkcell.travelguideapp.model.Priority
import com.turkcell.travelguideapp.model.Visitation

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
    fun returnAllPlaces(): ArrayList<Place> {
        val tmpList = ArrayList<Place>()
        var tmpPlace: Place

        open()
        val query = "SELECT * FROM Place"
        val c: Cursor = travelGuideDatabase!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {

                tmpPlace = Place(
                    c.getString(c.getColumnIndex("Name")),
                    LatLng(
                        c.getString(c.getColumnIndex("Latitude")).toDouble(),
                        c.getString(c.getColumnIndex("Longitude")).toDouble()
                    ),
                    c.getString(c.getColumnIndex("Definition")),
                    c.getString(c.getColumnIndex("Description")),
                    Priority.valueOf(c.getString(c.getColumnIndex("Priority")))
                ).apply {
                    this.id = c.getInt(0)
                }

                tmpList.add(tmpPlace)
            } while (c.moveToNext())
        }

        c.close()
        close()
        return tmpList
    }

    fun addVisitation(v: Visitation, placeId: Int): Long {
        val cv = ContentValues()
        cv.put("Date", v.date)
        cv.put("Description", v.description)
        cv.put("PlaceId", placeId)

        open()
        var output: Long = -1
        try {
            output = travelGuideDatabase!!.insert("Visitation", null, cv)
        } catch (error: SQLiteException){
            Log.e("Exception", "SQLException" + error.localizedMessage)
        }
        close()
        return output
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


    /*@SuppressLint("Range")
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
        val query = "Select * from Place"
        return travelGuideDatabase!!.rawQuery(query, null)
    }*/

    @SuppressLint("Range")
    fun getVisitationsByPlaceId(id: Int?): ArrayList<Visitation>{
        val visitationList = ArrayList<Visitation>()
        var visitation : Visitation
        open()

        var cursor : Cursor = getVisitations(id!!)
        if(cursor.moveToFirst()){

            do {
                val date = cursor.getString(cursor.getColumnIndex("Date"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                visitation = Visitation(date, description)
                visitation.id = cursor.getInt(0)

                visitationList.add(visitation)
            }while (cursor.moveToNext())

        }
        close()
        return visitationList
    }

    private fun getVisitations(id: Int) : Cursor {
        val query = "Select * from Visitation where PlaceId = '$id'"
        return travelGuideDatabase!!.rawQuery(query, null)
    }
}