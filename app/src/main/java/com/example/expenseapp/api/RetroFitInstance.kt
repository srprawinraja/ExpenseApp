package com.example.expenseapp.api

import com.example.expenseapp.api.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java


object RetroFitInstance{
    const val BASE_URL: String = "http://10.0.2.2:5000/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiServiceGetInstance: ApiService = retrofit.create(ApiService::class.java)
}
