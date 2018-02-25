package com.jhr.abdallahsarayrah.findmycar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by abdallah.sarayrah on 12/20/2017.
 */
class LocationDB(context: Context) : SQLiteOpenHelper(context, "location.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table location(lat double unique, lon double unique)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}