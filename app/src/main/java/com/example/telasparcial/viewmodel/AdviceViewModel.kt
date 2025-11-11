package com.example.telasparcial.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telasparcial.data.RetroFitInstance
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class AdviceViewModel : ViewModel(){

    val advice = mutableStateOf("Carregando")

    init {
        fetchAdvice()
    }
    fun fetchAdvice(){
        viewModelScope.launch {

            try {
                val response = RetroFitInstance.api.getAdvice()
                advice.value = response.slip.advice

            }catch (e: Exception){
                advice.value = ">>Error ${e.message}<<"
            }
        }
    }
}