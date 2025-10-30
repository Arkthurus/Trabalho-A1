package com.example.telasparcial.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Grupos")
data class Grupo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String
)