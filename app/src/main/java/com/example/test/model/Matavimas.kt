package com.example.test.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity(tableName = "matavimai")
@Serializable
data class Matavimas(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "matavimas")
    @SerializedName("matavimas")
    val matavimas: String,

    @ColumnInfo(name = "x")
    @SerializedName("x")
    val x: Int,

    @ColumnInfo(name = "y")
    @SerializedName("y")
    val y: Int,

    @ColumnInfo(name = "atstumas")
    @SerializedName("atstumas")
    val atstumas: Double,

    @ColumnInfo(name = "aggregatedStrengths")
    @SerializedName("aggregatedStrengths")
    val aggregatedStrengths: MutableMap<String, Int> = mutableMapOf()
) {
    fun aggregateStrength(sensor: String, strength: Int) {
        aggregatedStrengths[sensor] = strength
    }
}
