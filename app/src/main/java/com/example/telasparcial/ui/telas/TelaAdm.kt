package com.example.telasparcial.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.telasparcial.ui.viewmodel.AuthViewModel

// Mock de dados para a UI (será substituído pela lógica real)
data class UserAdm(
    val uid: String,
    val email: String,
    val isAdm: Boolean = false // Poderíamos adicionar um campo para diferenciar Adm
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdm(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // ⚠️ ATENÇÃO: Esta é uma lista MOCK para a UI.
    // Substituiremos por um Flow de usuários reais do Firebase ou Room na etapa de Lógica.
    val mockUsers = listOf(
        UserAdm(uid = "uid123", email = "admin@sistema.com", isAdm = true),
        UserAdm(uid = "uid456", email = "usuarioA@teste.com"),
        UserAdm(uid = "uid789", email = "usuarioB@teste.com"),
        UserAdm(uid = "uid101", email = "heitor@teste.com"),
        // Adicionar um placeholder para mostrar o usuário logado (se for o Adm)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Painel Administrativo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Lista de Usuários Cadastrados",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            // Lista de Usuários
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(mockUsers) { user ->
                    UserListItem(user = user) {
                        // Ação futura: Navegar para tela de edição/detalhes do usuário
                        // navController.navigate("TelaEditUser/${user.uid}")
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: UserAdm, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (user.isAdm) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "UID: ${user.uid.take(10)}...", // Mostrar apenas o início do UID
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (user.isAdm) {
                Text(
                    text = "ADMIN",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}