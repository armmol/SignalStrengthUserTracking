package com.example.test.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.test.model.Matavimas
import com.example.test.model.Stiprumai
import com.example.test.model.Vartotojai

@Dao
interface AppDao {
    @Upsert
    suspend fun addStiprumaiToRoom(stiprumai: Stiprumai)

    @Query("DELETE FROM stiprumai")
    suspend fun deleteAllStiprumaiFromRoom()

    @Query("SELECT * FROM stiprumai")
    suspend fun getStiprumai(): List<Stiprumai>

    @Upsert
    suspend fun addMatavimasToRoom(matavimas: Matavimas)

    @Query("DELETE FROM matavimai")
    suspend fun deleteAllMatavimaiFromRoom()

    @Query("SELECT * FROM matavimai")
    suspend fun getMatavimas(): List<Matavimas>

    @Upsert
    suspend fun addVartotojaiToRoom(vartotojai: Vartotojai)

    @Query("DELETE FROM vartotojai")
    suspend fun deleteAllVartotojaiFromRoom()

    @Query("SELECT * FROM vartotojai")
    suspend fun getVartotojai(): List<Vartotojai>
}