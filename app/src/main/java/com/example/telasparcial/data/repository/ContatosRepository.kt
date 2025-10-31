package com.example.telasparcial.data.repository

import com.example.telasparcial.data.dao.ContatosDAO
import com.example.telasparcial.data.entities.Contato
import kotlinx.coroutines.flow.Flow

class ContatosRepository (private val contatosDAO: ContatosDAO){

    suspend fun salvarContato(contato: Contato){
        contatosDAO.salvarContato(contato)
    }

    fun buscarTodos(): Flow<List<Contato>> {
        return contatosDAO.buscarTodos()
    }

    fun buscarQTD(quantidade: Int): Flow<List<Contato>> {
        return contatosDAO.buscar(quantidade)
    }

    suspend fun atualizarContato(contato: Contato){
        contatosDAO.atualizarContato(contato)
    }

    suspend fun deletarContato(contato: Contato){
        contatosDAO.deletarContato(contato)
    }
}
