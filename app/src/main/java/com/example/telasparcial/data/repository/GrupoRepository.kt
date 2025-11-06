package com.example.telasparcial.data.repository

import android.util.Log
import com.example.telasparcial.data.dao.GrupoDAO
import com.example.telasparcial.data.entities.Grupo
import kotlinx.coroutines.flow.Flow

class GrupoRepository (private val grupoDAO: GrupoDAO){

    suspend fun inserirGrupo(grupo: Grupo){
        grupoDAO.inserirGrupo(grupo)
    }

    suspend fun deletarGrupo(grupo: Grupo){
        grupoDAO.deletarGrupo(grupo)
    }

    suspend fun buscarTodos(): Flow<List<Grupo>>{
        return grupoDAO.buscarTodos()
    }

    suspend fun buscarPeloNome(nome: String): Grupo?{
        return grupoDAO.buscarPeloNome(nome)
    }
}