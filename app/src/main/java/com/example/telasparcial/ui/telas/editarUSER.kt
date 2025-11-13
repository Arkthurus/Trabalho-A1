package com.example.telasparcial.ui.telas


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation // Import necessário
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.telasparcial.viewmodel.AuthViewModel
import com.example.telasparcial.viewmodel.PreferencesUiState

@Composable
fun EditUSER(
    navController: NavController,
    authViewModel: AuthViewModel,
    preferencesUiState: PreferencesUiState
) {
    // 1. Coletar o estado do usuário e de loading do ViewModel
    val userState by authViewModel.userState.collectAsStateWithLifecycle()
    val isLoading by authViewModel.loading.collectAsStateWithLifecycle()
    val feedbackMsg by authViewModel.authFeedback.collectAsStateWithLifecycle() // Mantido para lógica de popBackStack

    val requiresReAuth by authViewModel.requiresReAuth.collectAsStateWithLifecycle()

    // Variáveis de estado para os campos de texto
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val context = LocalContext.current

    var showReAuthDialog by remember { mutableStateOf(false) }
    var senhaAtual by remember { mutableStateOf("") }

    LaunchedEffect(requiresReAuth) {
        if (requiresReAuth){
            showReAuthDialog = true
        }
    }

    // 2. LaunchedEffect para preencher os campos quando a tela carregar
    LaunchedEffect(userState) {
        userState?.email?.let { currentUserEmail ->
            email = currentUserEmail
        }
    }

    // 3. LaunchedEffect para observar o feedback (somente para lógica de navegação)
    LaunchedEffect(feedbackMsg) {
        feedbackMsg?.let {
            // Se a atualização foi bem-sucedida, navega de volta
            // A exibição do Toast em si deve ser feita na AppNav para globalidade
            if (it.contains("sucesso", ignoreCase = true)) {
                navController.popBackStack()
            }
            // Não limpamos o feedback aqui, pois o AppNav já o limpa após exibir o Toast global
        }
    }

    // --- DIÁLOGO DE REAUTENTICAÇÃO ---
    if (showReAuthDialog) {
        AlertDialog(
            onDismissRequest = {
                showReAuthDialog = false
                authViewModel.reAuthHandled()
            },
            containerColor = preferencesUiState.corDeFundo,
            title = { Text("Autenticação Necessária", color = preferencesUiState.corDeTexto) },
            text = {
                Column {
                    Text("Por segurança, digite sua senha atual para confirmar a alteração.", color = preferencesUiState.corDeTexto)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = senhaAtual,
                        onValueChange = { senhaAtual = it },
                        label = { Text("Senha Atual", color = preferencesUiState.corDeTexto) },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    authViewModel.reauthenticateAndRetryUpdate(senhaAtual, email, senha)
                    showReAuthDialog = false // Fecha o diálogo
                    authViewModel.reAuthHandled()
                    senhaAtual = "" // Limpa o campo
                },
                    colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao)) {
                    Text("Confirmar", color = preferencesUiState.corDeTexto)
                }
            },
            dismissButton = {
                Button(onClick = {
                    showReAuthDialog = false
                    authViewModel.reAuthHandled()
                    senhaAtual = ""
                },
                    colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao)) {
                    Text("Cancelar", color = preferencesUiState.corDeTexto)
                }
            }
        )
    }

    // --- UI da Tela ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = preferencesUiState.corDeFundo)
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

    ) {
        // Mostra um indicador de progresso se estiver carregando
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = "Editar Perfil", style = MaterialTheme.typography.titleLarge, color = preferencesUiState.corDeTexto)

            Spacer(modifier = Modifier.height(32.dp))

            val colors = TextFieldDefaults.colors(
                focusedTextColor = preferencesUiState.corDeTexto,
                unfocusedTextColor = preferencesUiState.corDeTexto.copy(alpha = .3f),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )

            // Campo de texto para o E-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail", color = preferencesUiState.corDeTexto) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = colors

                // Adicionado 'enabled = false' se o e-mail não puder ser alterado diretamente
                // ou removido se puder, mas a validação de 'user.email != email' já faz o controle
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para a Nova Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Nova Senha (deixe em branco se não for alterar)", color = preferencesUiState.corDeTexto.copy(alpha = .3f)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                colors = colors
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    authViewModel.atualizaruser(email, senha)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading, // Desabilita o botão durante o carregamento
                colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao)
            ) {
                Text("Salvar Alterações", color = preferencesUiState.corDeTexto)
            }
        }
    }
}