package com.example.telasparcial.data.repository

import android.util.Log
import com.example.telasparcial.data.dao.GrupoContatoDAO
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo
import com.example.telasparcial.data.entities.pojos.GrupoComContatos
import kotlinx.coroutines.flow.Flow


class GrupoContatoRepository (private val grupoContatoDAO: GrupoContatoDAO){ // Nome da variável: grupoContatoDAO

    fun buscarTodos(): Flow<List<GrupoComContatos>>{
        return grupoContatoDAO.buscarTodos()
    }

    suspend fun adicionarAoGrupo(grupo: Grupo, contato: Contato){
        grupoContatoDAO.adicionarAoGrupo(grupo.id,contato.id)
    }

    suspend fun removerDoGrupo(grupoId: Int, contatoId: Int) {
        try {
            // 🐛 CORREÇÃO: Usando o nome correto da variável: grupoContatoDAO
            grupoContatoDAO.removerLigacao(grupoId, contatoId)
        } catch (e: Exception) {
            // ✅ TRATAMENTO DE EXCEÇÃO APLICADO
            Log.e("GrupoContatoRepository", "Erro no DAO ao remover ligação: ${e.message}")
            throw e // Relança para ser tratado no ViewModel, se necessário
        }
    }
}