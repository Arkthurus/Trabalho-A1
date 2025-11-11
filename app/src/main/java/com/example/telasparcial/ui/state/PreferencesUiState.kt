package com.example.telasparcial.ui.state

import androidx.compose.ui.graphics.Color

enum class TamanhoDeFonte(val tamanho: Float) {
    Pequeno(tamanho = 18f),
    Medio(tamanho = 20f),
    Grande(tamanho = 22f);
}

data class PreferencesUiState(
    val corDeTexto: Color,
    val corDeFundo: Color,
    val corDeBotao: Color,
    val tamanhoDeFonte: TamanhoDeFonte = TamanhoDeFonte.Medio
)