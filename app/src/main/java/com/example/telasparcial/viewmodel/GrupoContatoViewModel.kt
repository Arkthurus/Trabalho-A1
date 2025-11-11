package com.example.telasparcial.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo
import com.example.telasparcial.data.entities.pojos.GrupoComContatos
import com.example.telasparcial.data.repository.GrupoContatoRepository
import com.example.telasparcial.data.repository.GrupoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GruposContatosUiState(
    val gruposComContatos: List<GrupoComContatos> = emptyList()
    val gruposComContatos: List<GrupoComContatos> = emptyList(),
    val feedbackMessage: StateFlow<String?>
) {}

class GrupoContatoViewModel(private val grupoContatoRepository: GrupoContatoRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(GruposContatosUiState())

class GrupoContatoViewModel(
    private val grupoContatoRepository: GrupoContatoRepository
) : ViewModel() {
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(
        GruposContatosUiState(
            feedbackMessage = _feedbackMessage.asStateFlow()
        )
    )
    val uiState: StateFlow<GruposContatosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            grupoContatoRepository.buscarTodos().collect() { gruposComContatos ->
                _uiState.update {
                    it.copy(gruposComContatos = gruposComContatos)
            try {
                grupoContatoRepository.buscarTodos().collect() { gruposComContatos ->
                    _uiState.update {
                        it.copy(gruposComContatos = gruposComContatos)
                    }
                }
            } catch (e: Exception) {
                _feedbackMessage.value = "Erro ao buscar grupos e contatos: ${e.message}"
            }
        }
    }

    fun adicionarAoGrupo(grupo: Grupo?, contato: Contato) {

        if (grupo==null) {
            Log.e("GrupoContatoViewModel", "Grupo não existe")
        if (grupo == null) {
            _feedbackMessage.value = "Grupo não existe"
            return
        }

        val state = _uiState.value

        val grupoComContatos = state.gruposComContatos.find { it.grupo.id == grupo.id }



        if (grupoComContatos == null) {
            Log.e("GrupoContatoViewModel", "Grupo não encontrado")
            _feedbackMessage.value = "Grupo não encontrado"
            return
        }

        if (grupoComContatos.contatos.any { it -> it.id == contato.id }) {
            Log.e("GrupoContatoViewModel", "Contato já está no grupo")
            _feedbackMessage.value = "\"${contato.nome}\" já está no grupo \"${grupo.nome}\""
            return
        }

        viewModelScope.launch {
            grupoContatoRepository.adicionarAoGrupo(grupo, contato)
            try {
                grupoContatoRepository.adicionarAoGrupo(grupo, contato)
            } catch (e: Exception) {
                _feedbackMessage.value = "Erro ao adicionar \"${contato.nome}\" ao grupo \"${grupo.nome}\": ${e.message}"
            }
        }
    }

    fun removerDoGrupo(grupo: Grupo, contato: Contato) {
        viewModelScope.launch {
            try {
                // Aqui você chama o Repositório, que por sua vez chamará o DAO
                grupoContatoRepository.removerDoGrupo(grupo.id, contato.id)
                Log.d("GrupoContatoViewModel", "${contato.nome} removido do grupo ${grupo.nome}.")
                _feedbackMessage.value = "\"${contato.nome}\" removido do grupo \"${grupo.nome}\""
            } catch (e: Exception) {
                Log.e("GrupoContatoViewModel", "Erro ao remover do grupo: ${e.message}")
                _feedbackMessage.value =
                    "Erro ao remover \"${contato.nome}\" do grupo \"${grupo.nome}\": ${e.message}"
            }
        }
    }

    fun clearFeedbackMessage() {
        _feedbackMessage.value = null
    }
}

class GrupoContatoViewModelFactory(private val grupoContatoRepository: GrupoContatoRepository) : ViewModelProvider.Factory {
class GrupoContatoViewModelFactory(private val grupoContatoRepository: GrupoContatoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GrupoContatoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GrupoContatoViewModel(grupoContatoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}