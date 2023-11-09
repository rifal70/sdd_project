package com.example.sdd_project.api

import com.example.sdd_project.data.PromoData
import retrofit2.http.GET

interface ApiService {
    @GET("promos")
    suspend fun fetchPromos(): PromoData
}