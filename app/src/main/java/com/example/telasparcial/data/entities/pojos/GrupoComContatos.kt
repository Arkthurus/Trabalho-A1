package com.example.telasparcial.data.entities.pojos

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.data.entities.Grupo
import com.example.telasparcial.data.entities.GrupoContato

data class GrupoComContatos(
    @Embedded val grupo: Grupo,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",

        associateBy = Junction(
            value = GrupoContato::class,
            parentColumn = "grupoId",
            entityColumn = "contatoId"
        )
    )
    val contatos: List<Contato>
)