package com.example.vassuApp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vassuApp.model.Matavimas
import com.example.vassuApp.model.Stiprumai
import com.example.vassuApp.model.Vartotojai

@Database(
    entities = [Stiprumai::class, Matavimas::class, Vartotojai::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPortalDao(): AppDao
}
