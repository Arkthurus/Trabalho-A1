package com.example.telasparcial.data.repository

import android.util.Log
import com.example.telasparcial.ui.telas.UserAdm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminRepository(private val fireStore: FirebaseFirestore, private val auth: FirebaseAuth) {

    // Função existente: verifica se o usuário logado está na coleção 'admins'
    suspend fun isAdmin(): Boolean {
        val currentUser = auth.currentUser;
        val userId = currentUser?.uid;

        if (userId == null) {
            Log.i(AdminRepository::class.simpleName, "Usuário ainda não autenticado. Retornando false.")
            return false;
        }

        return try {

            val results = fireStore.collection("admins")
                .where(Filter.equalTo("userId", userId))
                .limit(1)
                .get()
                .await()
                .documents;

            !results.isEmpty();
        } catch (e: Exception) {
            Log.e("AdminRepository", "Erro ao verificar se o usuário é admin: ${e.message}")
            false
        }
    }

    // =========================================================
    // >> MÉTODO AJUSTADO PARA BUSCAR TODOS OS USUÁRIOS
    // =========================================================
    suspend fun getAllUsers(): List<UserAdm> {
        return try {
            // 1. Busca a lista de UIDs que são administradores
            val adminSnapshot = fireStore.collection("admins")
                .get()
                .await()

            // Cria um Set (conjunto) dos UIDs de todos os administradores para busca rápida
            val adminUids = adminSnapshot.documents.mapNotNull { it.getString("userId") }.toSet()

            // 2. Busca todos os usuários da coleção "users"
            val userSnapshot = fireStore.collection("users")
                .get()
                .await()

            // 3. Mapeia e cruza as informações
            userSnapshot.documents.map { doc ->
                val uid = doc.id

                UserAdm(
                    uid = uid,
                    email = doc.getString("email") ?: "N/A",
                    // Verifica se o UID do usuário está no conjunto de UIDs de admin
                    isAdm = adminUids.contains(uid)
                )
            }.filter { it.email != "N/A" } // Filtra entradas sem e-mail, se necessário

        } catch (e: Exception) {
            Log.e("AdminRepository", "Erro ao buscar todos os usuários: ${e.message}")
            emptyList()
        }
    }
}