package com.example.telasparcial.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "GruposContatos",
    primaryKeys = ["grupoId", "contatoId"],
    foreignKeys = [
        ForeignKey(
            entity = Grupo::class,
            parentColumns = ["id"],
            childColumns = ["grupoId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Contato::class,
            parentColumns = ["id"],
            childColumns = ["contatoId"],
            onDelete = CASCADE
        )
    ]
)
data class GrupoContato(
    val grupoId: Int,
    @ColumnInfo(index = true)
    val contatoId: Int
)