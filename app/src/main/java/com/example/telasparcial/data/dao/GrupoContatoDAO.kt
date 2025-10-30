package com.example.telasparcial.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.telasparcial.data.entities.GrupoContato
import com.example.telasparcial.data.entities.pojos.GrupoComContatos
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoContatoDAO {
    @Transaction
    @Query("SELECT * FROM Grupos")
    fun buscarTodos(): Flow<List<GrupoComContatos>>

    @Transaction
    @Query("SELECT * FROM Grupos WHERE id = :idGrupo")
    fun buscarPorId(idGrupo: Int): Flow<GrupoComContatos?>

    @Transaction
    suspend fun adicionarAoGrupo(grupoId: Int, contatoId: Int) {
        val grupoContato = GrupoContato(
            grupoId = grupoId,
            contatoId = contatoId
        )
        inserirGrupoContato(grupoContato)
    }

    @Insert
    suspend fun inserirGrupoContato(grupoContato: GrupoContato)
}