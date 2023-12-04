package com.example.vassuApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity(tableName = "stiprumai")
@Serializable
data class Stiprumai(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "matavimas")
    @SerializedName("matavimas")
    val matavimas: Int,

    @ColumnInfo(name = "sensorius")
    @SerializedName("sensorius")
    val sensorius: String,

    @ColumnInfo(name = "stiprumas")
    @SerializedName("stiprumas")
    val stiprumas: Int
)