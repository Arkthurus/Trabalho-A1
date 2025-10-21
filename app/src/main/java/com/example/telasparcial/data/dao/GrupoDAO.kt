package com.example.telasparcial.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.telasparcial.data.entities.Grupo
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDAO {
    @Insert
    suspend fun inserirGrupo(grupo: Grupo)

    @Delete
    suspend fun deletarGrupo(grupo: Grupo)

    @Query("SELECT * FROM Grupos")
    fun buscarTodos(): Flow<List<Grupo>>

    @Query("SELECT * FROM Grupos WHERE nome = :nome LIMIT 1")
    suspend fun buscarPeloNome(nome: String): Grupo?
}