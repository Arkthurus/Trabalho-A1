package com.example.myapplication.mvvm.data

import com.example.myapplication.data.database.dao.CronogramaDAO
import com.example.myapplication.data.database.entities.Cronograma
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CronogramaRepository(private val cronogramaDAO: CronogramaDAO) {

    // 1. Lógica Principal: Busca ou Popula 30 dias se o banco estiver vazio
    suspend fun buscarOuCriarCronograma(): List<Cronograma> {
        return withContext(Dispatchers.IO) {
            val itemsExistentes = cronogramaDAO.getAll()

            if (itemsExistentes.size < 30) {
                val novosDias = (1..30).map { dia ->
                    Cronograma(
                        diaDoMes = dia,
                        nome = null,
                        horario = null,
                        descricao = null
                    )
                }
                cronogramaDAO.insertAll(novosDias)
                // Retorna a lista completa após a inserção
                cronogramaDAO.getAll()
            } else {
                // Retorna a lista existente
                itemsExistentes
            }
        }
    }

    // 2. Função de Atualização
    suspend fun atualizar(item: Cronograma) {
        cronogramaDAO.update(item)
    }

    // 3. Função de Deleção
    suspend fun deletar(item: Cronograma) {
        cronogramaDAO.delete(item)
    }
}