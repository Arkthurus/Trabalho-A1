package com.example.telasparcial.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.telasparcial.data.dao.ContatosDAO
import com.example.telasparcial.data.dao.GrupoContatoDAO
import com.example.telasparcial.data.dao.GrupoDAO
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo
import com.example.telasparcial.data.entities.GrupoContato
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [
    Contato::class,
    Grupo::class,
    GrupoContato::class
    ], version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contatosDao(): ContatosDAO
    abstract fun grupoContatoDao(): GrupoContatoDAO
    abstract fun grupoDao(): GrupoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            } else {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).fallbackToDestructiveMigration(true).build()
                    INSTANCE = instance
                    CoroutineScope(Dispatchers.IO).launch {
                        if (instance.grupoDao().buscarPeloNome("Favoritos") == null){
                            instance.grupoDao().inserirGrupo(Grupo(nome = "Favoritos"))
                        }
                    }
                    return instance
                }
            }
        }
    }
}
