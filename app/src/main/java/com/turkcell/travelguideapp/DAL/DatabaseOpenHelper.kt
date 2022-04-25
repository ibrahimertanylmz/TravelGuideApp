package com.turkcell.travelguideapp.DAL

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        val queryForeignKey = "PRAGMA foreign_keys = ON"
        db.execSQL(queryForeignKey)
        val queryPlace =
            "CREATE TABLE Place(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Name TEXT, Latitude TEXT, Longitude TEXT, Description TEXT, Definition TEXT, Priority TEXT)"
        db.execSQL(queryPlace)
        val queryVisitation =
            "CREATE TABLE Visitation(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Date TEXT, Description TEXT, FOREIGN KEY(PlaceId) REFERENCES Place(Id) ON DELETE CASCADE)"
        db.execSQL(queryVisitation)
        val queryImage =
            "CREATE TABLE Image(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Data BLOB, FOREIGN KEY(PlaceId) REFERENCES Place(Id), FOREIGN KEY(VisitationId) REFERENCES Visitation(Id) )"
        db.execSQL(queryImage)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1) {

        }
    }


}
