package com.example.telasparcial.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo
import com.example.telasparcial.data.entities.pojos.GrupoComContatos
import com.example.telasparcial.data.repository.GrupoContatoRepository
import com.example.telasparcial.ui.state.GruposContatosUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GrupoContatoViewModel(private val grupoContatoRepository: GrupoContatoRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(GruposContatosUiState())

    val uiState: StateFlow<GruposContatosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            grupoContatoRepository.buscarTodos().collect() { gruposComContatos ->
                _uiState.update {
                    it.copy(gruposComContatos = gruposComContatos)
                }
            }
        }
    }

    fun adicionarAoGrupo(grupo: Grupo?, contato: Contato) {

        if (grupo==null) {
            Log.e("GrupoContatoViewModel", "Grupo não existe")
            return
        }

        val state = _uiState.value

        val grupoComContatos = state.gruposComContatos.find { it.grupo.id == grupo.id }



        if (grupoComContatos == null) {
            Log.e("GrupoContatoViewModel", "Grupo não encontrado")
            return
        }

        if (grupoComContatos.contatos.any { it -> it.id == contato.id }) {
            Log.e("GrupoContatoViewModel", "Contato já está no grupo")
            return
        }

        viewModelScope.launch {
            grupoContatoRepository.adicionarAoGrupo(grupo, contato)
        }
    }
    fun removerDoGrupo(grupo: Grupo, contato: Contato) {
        viewModelScope.launch {
            try {
                grupoContatoRepository.removerDoGrupo(grupo.id, contato.id)
                Log.d("GrupoContatoViewModel", "${contato.nome} removido do grupo ${grupo.nome}.")
            } catch (e: Exception) {
                Log.e("GrupoContatoViewModel", "Erro ao remover do grupo: ${e.message}")
            }
        }
    }
}

class GrupoContatoViewModelFactory(private val grupoContatoRepository: GrupoContatoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GrupoContatoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GrupoContatoViewModel(grupoContatoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
