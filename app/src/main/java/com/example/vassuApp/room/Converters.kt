package com.example.vassuApp.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String): MutableMap<String, Int> {
        val mapType = object : TypeToken<MutableMap<String, Int>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMap(map: MutableMap<String, Int>): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}
