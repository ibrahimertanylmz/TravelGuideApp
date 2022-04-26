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
    fun returnPlaces(): ArrayList<Place> {
        val tmpList = ArrayList<Place>()
        var tmpPlace: Place
        var tmpPriority: Priority = Priority.ONE

        open()

        val query = "SELECT * FROM Place"
        val c: Cursor = travelGuideDatabase!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {
                when (c.getString(c.getColumnIndex("Priority"))) {
                    "ONE" -> tmpPriority = Priority.ONE
                    "TWO" -> tmpPriority = Priority.TWO
                    "THREE" -> tmpPriority = Priority.THREE
                }

                tmpPlace = Place(
                    c.getString(c.getColumnIndex("Name")),
                    LatLng(
                        c.getString(c.getColumnIndex("Latitude")).toDouble(),
                        c.getString(c.getColumnIndex("Longitude")).toDouble()
                    ),
                    c.getString(c.getColumnIndex("Definition")),
                    c.getString(c.getColumnIndex("Description")),
                    tmpPriority
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


}