package com.example.myapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.database.dao.CronogramaDAO
import com.example.myapplication.data.database.dao.UsuarioDAO
import com.example.myapplication.data.database.entities.Cronograma
import com.example.myapplication.data.database.entities.Usuario

// 1. Definição do banco de dados:
@Database(

    entities = [Usuario::class, Cronograma::class],

    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // 2. Método para acessar o DAO do Usuario
    abstract fun usuarioDAO(): UsuarioDAO

    // 3. Método para acessar o novo DAO do Cronograma
    abstract fun cronogramaDAO(): CronogramaDAO

    // 3. Companion Object para o padrão Singleton (Mantido)
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cateirinha_database"
                )
                    //Mantido para lidar com a migração (destrói e recria o banco)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}