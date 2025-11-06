package com.example.telasparcial.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminRepository(private val fireStore: FirebaseFirestore, private val auth: FirebaseAuth) {
    suspend fun isAdmin(): Boolean {
        val currentUser = auth.currentUser;
        val userId = currentUser?.uid;

        if (userId == null) {
            // Usuário ainda não autenticou
            Log.i(AdminRepository::class.simpleName, "Usuário ainda não autenticado. Retornando false.")
            return false;
        }

        return try {
            // Buscar o documento do usuário nas entradas de admins
            val results = fireStore.collection("admins")
                .where(Filter.equalTo("userId", userId))
                .limit(1)
                .get()
                .await()
                .documents;

            // Se ele não estiver vazio, quer dizer que ele foi encontrado :p
            !results.isEmpty();
        } catch (e: Exception) {
            Log.e("AdminRepository", "Erro ao verificar se o usuário é admin: ${e.message}")
            false
        }
    }
}