package com.example.primo_app_demo.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("feed/@primoapp")
    suspend fun getMediumFeed(): Response<ResponseBody>
}