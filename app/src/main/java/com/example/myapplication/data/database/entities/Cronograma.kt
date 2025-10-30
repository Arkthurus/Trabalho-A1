package com.example.myapplication.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tabela_cronograma")
    data class Cronograma(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val diaDoMes: Int,
    val nome: String?,
    val horario: String?,
    val descricao: String?
    )
