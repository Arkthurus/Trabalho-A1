package com.example.telasparcial.data.repository

import com.example.telasparcial.data.dao.ContatosDAO
import com.example.telasparcial.data.entities.Contato
import kotlinx.coroutines.flow.Flow

class ContatosRepository (private val contatosDAO: ContatosDAO){

    suspend fun salvarContato(contato: Contato){
        contatosDAO.salvarContato(contato)
    }

    suspend fun buscarTodos(): Flow<List<Contato>> {
        return contatosDAO.buscarTodos()
    }

    suspend fun buscarQTD(QTD: Int): Flow<List<Contato>> {
        return contatosDAO.buscar(QTD)
    }

    suspend fun atualizarContato(contato: Contato){
        contatosDAO.atualizarContato(contato)
    }

    suspend fun deletarContato(contato: Contato){
        contatosDAO.deletarContato(contato)
    }

}
