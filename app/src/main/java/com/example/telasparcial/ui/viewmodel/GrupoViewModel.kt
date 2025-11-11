package com.example.telasparcial.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo
import com.example.telasparcial.data.repository.GrupoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class  GrupoUiState(
    val listaDeGrupos: List<Grupo> = emptyList(),
    val nome:   String = "",
    val id: Int = 0,
    val contatoEmEdit: Contato? = null
){}

class GrupoViewModel (private val grupoRepository: GrupoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(GrupoUiState())

    val uiState: StateFlow<GrupoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            grupoRepository.buscarTodos().collect { grupos ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeGrupos = grupos)
                }
            }
        }
    }

    fun inserirGrupo(grupo: Grupo){

        if (grupo.nome.isBlank()) return

        viewModelScope.launch {
            grupoRepository.inserirGrupo(grupo)
        }
    }

    fun deletarGrupo(grupo: Grupo){
        viewModelScope.launch {
            grupoRepository.deletarGrupo(grupo)
        }
    }

    suspend fun buscarPeloNome(nome: String): Grupo?{
        return grupoRepository.buscarPeloNome(nome)
    }
}

class GrupoViewModelFactory(private val grupoRepository: GrupoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GrupoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GrupoViewModel(grupoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}