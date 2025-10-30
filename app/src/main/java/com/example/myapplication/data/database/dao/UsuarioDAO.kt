package com.example.myapplication.data.database.dao



import kotlinx.coroutines.flow.Flow
import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.database.entities.Usuario

@Dao
interface UsuarioDAO {


    @Query("SELECT * FROM tabela_usuarios LIMIT 1")
    suspend fun buscarUsuarioUnico(): Usuario?
    /**
     * Insere um novo usuário na tabela.
     * @param usuario O objeto Usuario a ser inserido.
     */
    @Insert
    suspend fun inserir(usuario: Usuario)

    /**
     * Atualiza um usuário existente na tabela.
     * @param usuario O objeto Usuario a ser atualizado (baseado no ID).
     */
    @Update
    suspend fun atualizar(usuario: Usuario)

    /**
     * Deleta um usuário da tabela.
     * @param usuario O objeto Usuario a ser deletado (baseado no ID).
     */
    @Delete
    suspend fun deletar(usuario: Usuario)

    /**
     * Busca todos os usuários na tabela.
     * @return Uma lista de todos os objetos Usuario, ordenados por ID.
     */
    @Query("SELECT * FROM tabela_usuarios ORDER BY id ASC")
    suspend fun buscarTodos(): List<Usuario>

    /**
     * Busca um usuário específico pelo ID.
     * @param idUsuario O ID do usuário a ser buscado.
     * @return O objeto Usuario correspondente, ou null se não for encontrado.
     */
    @Query("SELECT * FROM tabela_usuarios WHERE id = :idUsuario")
    suspend fun buscarPorId(idUsuario: Int): Usuario?

    // Método reativo do Room para MVVM
    @Query("SELECT * FROM tabela_usuarios LIMIT 1") // Se o nome da sua tabela é 'tabela_usuarios'
    fun buscarUsuarioUnicoFlow(): Flow<Usuario?>
}