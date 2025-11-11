package com.example.telasparcial.ui.state

import com.example.telasparcial.data.entities.pojos.GrupoComContatos

data class GruposContatosUiState(
    val gruposComContatos: List<GrupoComContatos> = emptyList()
)