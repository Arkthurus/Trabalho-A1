package com.example.telasparcial.data.repository

import com.example.telasparcial.data.dao.GrupoContatoDAO
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo
import com.example.telasparcial.data.entities.pojos.GrupoComContatos
import kotlinx.coroutines.flow.Flow

class GrupoContatoRepository (private val grupoContatoDAO: GrupoContatoDAO){

    fun buscarTodos(): Flow<List<GrupoComContatos>>{
        return grupoContatoDAO.buscarTodos()
    }

    suspend fun adicionarAoGrupo(grupo: Grupo, contato: Contato){
        grupoContatoDAO.adicionarAoGrupo(grupo.id,contato.id)
    }
}