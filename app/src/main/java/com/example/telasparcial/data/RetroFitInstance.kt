package com.example.telasparcial.data

import com.example.telasparcial.data.dao.AdviceApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetroFitInstance{

    private const val BASE_URL = "https://api.adviceslip.com/"

    val api: AdviceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdviceApiService::class.java)
    }
}