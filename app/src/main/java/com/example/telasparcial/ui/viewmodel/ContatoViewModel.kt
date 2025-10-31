package com.example.telasparcial.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.repository.ContatosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class  ContatosUiState(
    val listaDeContatos: List<Contato> = emptyList(),
    val lista4Contatos:  List<Contato> = emptyList(),
    val nome:   String = "",
    val numero: String = " ",
    val id: Int = 0,
    val contatoEmEdit: Contato? = null
)


class ContatoViewModel (private val contatosRepository: ContatosRepository): ViewModel() {

    private val _uiState = MutableStateFlow(ContatosUiState())

    val uiState: StateFlow<ContatosUiState> = _uiState.asStateFlow()

    init {
        // Coleta todos os contatos do repositório
        viewModelScope.launch {
            contatosRepository.buscarTodos().collect { contatos ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeContatos = contatos)
                }
            }
        }
        // Coleta os 4 contatos mais recentes/requisitados
        viewModelScope.launch {
            contatosRepository.buscarQTD(4).collect { contatos ->
                _uiState.update { currentState ->
                    currentState.copy(lista4Contatos = contatos)
                }
            }
        }
    }


    // ✅ NOVO MÉTODO: Centraliza a lógica de adicionar aos favoritos,
    // garantindo que as operações de I/O ocorram no ViewModel.
    fun adicionarAosFavoritos(
        contato: Contato,
        grupoViewModel: GrupoViewModel,
        grupoContatoViewModel: GrupoContatoViewModel
    ) {
        viewModelScope.launch {
            try {
                val grupoFavoritos = grupoViewModel.buscarPeloNome("Favoritos")

                grupoFavoritos?.let { grupo ->
                    grupoContatoViewModel.adicionarAoGrupo(grupo, contato)
                    // Log.d("ContatoViewModel", "${contato.nome} adicionado aos Favoritos.")
                }
            } catch (e: Exception) {
                Log.e("ContatoViewModel", "Erro ao adicionar aos favoritos: ${e.message}")
            }
        }
    }


    fun salvarContato(contato: Contato){
        if (contato.nome.isBlank() || contato.numero.isBlank()) return
        viewModelScope.launch {
            contatosRepository.salvarContato(contato)
        }
    }

    fun receberCttEdit(contato: Contato){
        _uiState.update {
            it.copy(
                contatoEmEdit = contato
            )
        }
    }

    fun atualizarContato(contato: Contato){
        if (contato.nome.isNotBlank() || contato.numero.isNotBlank()){
            // A UI é atualizada automaticamente devido à coleta do Flow no bloco init.
            viewModelScope.launch { contatosRepository.atualizarContato(contato) }
        }
    }

    fun deletarContato(contato: Contato){
        viewModelScope.launch {
            contatosRepository.deletarContato(contato)
        }
    }
}

class ContatosViewModelFactory(private val contatoRepository: ContatosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContatoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContatoViewModel(contatoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}