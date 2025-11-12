package com.example.telasparcial.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class TamanhoDeFonte(val tamanho: TextUnit) {
    Pequeno(tamanho = 18.sp),
    Medio(tamanho = 20.sp),
    Grande(tamanho = 22.sp);
}

data class PreferencesUiState(
    val corDeTexto: Color = Color(255, 255, 255),
    val corDeBotao: Color = Color(98, 170, 163),
    val corDeFundo: Color = Color(30,30,30),
    val corDeCards: Color = Color.LightGray,
    val tamanhoDeFonte: TamanhoDeFonte = TamanhoDeFonte.Medio,
)
class PreferencesViewModel : ViewModel() {

    // StateFlow privado que apenas o ViewModel pode modificar.
    private val _uiState = MutableStateFlow(PreferencesUiState())

    // StateFlow público e somente leitura para a UI observar as mudanças.
    val uiState: StateFlow<PreferencesUiState> = _uiState.asStateFlow()

    /**
     * Atualiza a cor do botão no estado da UI.
     * @param novaCor A nova cor a ser definida.
     */
    fun atualizarCorDoBotao(novaCor: Color) {
        _uiState.update { currentState ->
            currentState.copy(corDeBotao = novaCor)
        }
    }
}


