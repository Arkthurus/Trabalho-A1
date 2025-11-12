package com.example.telasparcial.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class TamanhoDeFonte(val tamanho: Float) {
    Pequeno(tamanho = 18f),
    Medio(tamanho = 20f),
    Grande(tamanho = 22f);
}

data class PreferencesUiState(
    val corDeTexto: Color = Color(255, 255, 255),
    val corDeBotao: Color = Color(98, 170, 163),
    val corDeFundo: Color = Color.Black,
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

    fun atualizarCorDoTexto(novaCor: Color) {
        _uiState.update { currentState ->
            currentState.copy(corDeTexto = novaCor)
        }
    }

    fun atualizarCorDoFundo(novaCor: Color) {
        _uiState.update { currentState ->
            currentState.copy(corDeFundo = novaCor)
        }
    }
}


