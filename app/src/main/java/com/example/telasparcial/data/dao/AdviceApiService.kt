package com.example.telasparcial.data.dao

import com.example.telasparcial.data.AdviceResponse
import retrofit2.http.GET

interface AdviceApiService{
    @GET("advice")
    suspend fun getAdvice(): AdviceResponse
}