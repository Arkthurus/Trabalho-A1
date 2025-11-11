package com.example.telasparcial.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.telasparcial.data.dao.ContatosDAO
import com.example.telasparcial.data.dao.GrupoContatoDAO
import com.example.telasparcial.data.dao.GrupoDAO
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo
import com.example.telasparcial.data.entities.GrupoContato
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Contato::class,
        Grupo::class,
        GrupoContato::class
    ], version = 6
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contatosDao(): ContatosDAO
    abstract fun grupoContatoDao(): GrupoContatoDAO
    abstract fun grupoDao(): GrupoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Se a instância ainda for nula após o synchronized, cria-a
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            )
                .fallbackToDestructiveMigration(false)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                val grupoDao = db.grupoDao()
                val grupoFavoritos = grupoDao.buscarPeloNome("Favoritos")

                if (grupoFavoritos == null) {
                    grupoDao.inserirGrupo(Grupo(nome = "Favoritos"))
                }
            }

            return db
        }
    }
}

private class DatabaseCallback(
    private val databaseInstance: AppDatabase?
) : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)

        CoroutineScope(Dispatchers.IO).launch {

            val grupoDao = databaseInstance?.grupoDao() ?: return@launch

            if (grupoDao.buscarPeloNome("Favoritos") == null) {
                grupoDao.inserirGrupo(Grupo(nome = "Favoritos"))
            }
        }
    }
}