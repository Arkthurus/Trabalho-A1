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

// >> NOVAS IMPORTAÇÕES NECESSÁRIAS
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

data class UserAdm(
    val uid: String,
    val email: String,
    val isAdm: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdm(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // 1. CHAME O MÉTODO DE BUSCA DE USUÁRIOS AO CARREGAR A TELA
    LaunchedEffect(Unit) {
        authViewModel.fetchRegisteredUsers()
    }

    // 2. COLETA O ESTADO (LISTA DE USUÁRIOS) DO VIEWMODEL
    val allUsers by authViewModel.allRegisteredUsers.collectAsStateWithLifecycle()
    val isLoading by authViewModel.loading.collectAsStateWithLifecycle()


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
                text = "Lista de Usuários Cadastrados (${allUsers.size})",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            // 3. EXIBIÇÃO CONDICIONAL DE CARREGAMENTO OU LISTA
            if (isLoading && allUsers.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Lista de Usuários
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 4. USA A LISTA REAL (allUsers)
                    items(allUsers) { user ->
                        UserListItem(user = user) {
                            // Ação futura: Navegar para tela de edição/detalhes do usuário
                        }
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