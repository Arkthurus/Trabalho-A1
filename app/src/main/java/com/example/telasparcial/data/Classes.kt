package com.example.telasparcial.data

data class AdviceResponse(
    val slip: Slip
)

data class Slip(
    val advice: String,
    val slip_id: String
)