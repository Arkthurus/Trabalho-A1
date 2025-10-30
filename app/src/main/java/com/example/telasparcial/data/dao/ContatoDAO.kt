package com.example.telasparcial.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.telasparcial.data.entities.Contato
import kotlinx.coroutines.flow.Flow

@Dao
interface ContatosDAO {
    @Insert
    suspend fun salvarContato(contato: Contato)

    @Query("SELECT * FROM Contatos")
    fun buscarTodos(): Flow<List<Contato>>

    @Query("SELECT * FROM Contatos ORDER BY id DESC LIMIT :quantidade")
    fun buscar(quantidade: Int): Flow<List<Contato>>

    @Delete
    suspend fun deletarContato(contato: Contato)

    @Update
    suspend fun atualizarContato(contato: Contato)
}