package com.example.test.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.test.model.Matavimas
import com.example.test.model.Stiprumai
import com.example.test.model.Vartotojai

@Database(
    entities = [Stiprumai::class, Matavimas::class, Vartotojai::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPortalDao(): AppDao
}
