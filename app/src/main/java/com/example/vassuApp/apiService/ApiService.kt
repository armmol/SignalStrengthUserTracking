package com.example.vassuApp.apiService

import com.example.vassuApp.model.Matavimas
import com.example.vassuApp.model.Stiprumai
import com.example.vassuApp.model.Vartotojai
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("stiprumai.php")
    suspend fun getStiprumai(): Response<List<Stiprumai>>

    @GET("matavimai.php")
    suspend fun getMatavimai(): Response<List<Matavimas>>

    @GET("vartotojai.php")
    suspend fun getVartotojai(): Response<List<Vartotojai>>
}