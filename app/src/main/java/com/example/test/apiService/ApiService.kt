package com.example.test.apiService

import com.example.test.model.Matavimas
import com.example.test.model.Stiprumai
import com.example.test.model.Vartotojai
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("get_stiprumai.php")
    suspend fun getStiprumai(): Response<List<Stiprumai>>

    @GET("get_matavimai.php")
    suspend fun getMatavimai(): Response<List<Matavimas>>

    @GET("get_vartotojai.php")
    suspend fun getVartotojai(): Response<List<Vartotojai>>
}