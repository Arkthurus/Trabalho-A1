package com.example.telasparcial.ui.state

import com.example.telasparcial.data.entities.Contato

data class  ContatosUiState(
    val listaDeContatos: List<Contato> = emptyList(),
    val lista4Contatos:  List<Contato> = emptyList(),
    val contatoEmEdit: Contato? = null
)
