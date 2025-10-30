package com.example.telasparcial.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Contatos")
data class Contato(
    @PrimaryKey(autoGenerate = true)
    val id      : Int = 0,
    val nome    : String,
    val numero  : String
)
