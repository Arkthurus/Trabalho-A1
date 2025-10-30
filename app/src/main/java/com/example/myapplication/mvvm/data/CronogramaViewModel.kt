package com.example.myapplication.mvvm.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.database.entities.Cronograma
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- UiState: Define o estado completo da tela de Cronograma ---
data class CronogramaUiState(
    val listaCronograma: List<Cronograma> = emptyList(),
    val isLoading: Boolean = true,
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
)

// --- ViewModel ---
class CronogramaViewModel(private val repository: CronogramaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CronogramaUiState())
    val uiState: StateFlow<CronogramaUiState> = _uiState.asStateFlow()

    init {
        // Inicia o carregamento da lista ao criar o ViewModel
        carregarCronograma()
    }

    fun carregarCronograma() {
        _uiState.update { it.copy(isLoading = true) } // Inicia o estado de carregamento
        viewModelScope.launch {
            try {
                val lista = repository.buscarOuCriarCronograma()
                _uiState.update {
                    it.copy(
                        listaCronograma = lista,
                        isLoading = false // Carregamento concluído com sucesso
                    )
                }
            } catch (e: Exception) {
                // Em caso de erro, apenas mostra a lista vazia e encerra o loading
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSave(itemEditado: Cronograma) {
        viewModelScope.launch {
            try {
                repository.atualizar(itemEditado)

                val mensagem = if (itemEditado.nome.isNullOrBlank() && itemEditado.horario.isNullOrBlank())
                    "Dia ${itemEditado.diaDoMes} limpo!"
                else
                    "Dia ${itemEditado.diaDoMes} atualizado!"

                // Recarrega a lista para refletir a mudança
                carregarCronograma()

                // Exibe o snackbar
                _uiState.update {
                    it.copy(
                        showSnackbar = true,
                        snackbarMessage = mensagem
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        showSnackbar = true,
                        snackbarMessage = "Erro ao salvar: ${e.message}"
                    )
                }
            }
        }
    }

    fun onSnackbarDismiss() = _uiState.update { it.copy(showSnackbar = false) }

}

// --- Factory ---
class CronogramaViewModelFactory(private val repository: CronogramaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CronogramaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CronogramaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}