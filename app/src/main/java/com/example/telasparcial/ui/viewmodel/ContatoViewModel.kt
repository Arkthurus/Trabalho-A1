package com.example.telasparcial.ui.viewmodel

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
    val contatoEmEdit: Contato? = null
){}



class ContatoViewModel (private val contatosRepository: ContatosRepository): ViewModel() {

    private val _uiState = MutableStateFlow(ContatosUiState())

    val uiState: StateFlow<ContatosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            contatosRepository.buscarTodos().collect { contatos ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeContatos = contatos)
                }
            }
        }
        viewModelScope.launch {
            contatosRepository.buscarQTD(4).collect { contatos ->
                _uiState.update { currentState ->
                    currentState.copy(lista4Contatos = contatos)
                }
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
