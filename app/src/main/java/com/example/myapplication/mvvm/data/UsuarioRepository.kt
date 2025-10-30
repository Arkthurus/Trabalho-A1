package com.example.myapplication.mvvm.data

import com.example.myapplication.data.database.dao.UsuarioDAO
import com.example.myapplication.data.database.entities.Usuario
import kotlinx.coroutines.flow.Flow

// Repository do Usuário, focado em operações CRUD
class UsuarioRepository(private val usuarioDAO: UsuarioDAO) {

    fun buscarUsuarioUnicoFlow(): Flow<Usuario?> {
        return usuarioDAO.buscarUsuarioUnicoFlow()
    }

    suspend fun inserir(usuario: Usuario) {
        usuarioDAO.inserir(usuario)
    }

    suspend fun atualizar(usuario: Usuario) {
        usuarioDAO.atualizar(usuario)
    }

    suspend fun deletar(usuario: Usuario) {
        usuarioDAO.deletar(usuario)
    }

}