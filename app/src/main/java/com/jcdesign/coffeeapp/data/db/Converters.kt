package com.jcdesign.coffeeapp.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.jcdesign.coffeeapp.data.network.response.location.Point


class Converters {

    @TypeConverter
    fun fromPoint(point: Point): String {
        val gson = Gson()
        return gson.toJson(point)
    }

    @TypeConverter
    fun toPoint(json: String): Point {
        val gson = Gson()
        return gson.fromJson(json, Point::class.java)
    }
}