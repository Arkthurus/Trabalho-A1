package com.example.telasparcial.ui.state

import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo


data class GrupoUiState(
    val listaDeGrupos: List<Grupo> = emptyList(),
    val nome: String = "",
    val id: Int = 0,
    val contatoEmEdit: Contato? = null
)