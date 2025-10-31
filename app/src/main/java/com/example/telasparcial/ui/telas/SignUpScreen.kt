package com.example.telasparcial.ui.telas


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.telasparcial.ui.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit // Função para navegar de volta ao login
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }

    // 1. Coletar o estado de loading e feedback
    val isLoading by authViewModel.loading.collectAsStateWithLifecycle()
    val feedbackMsg by authViewModel.authFeedback.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 2. Efeito para navegar de volta ao Login após sucesso
    LaunchedEffect(feedbackMsg) {
        feedbackMsg?.let { msg ->
            // Se a mensagem de feedback for de sucesso, navega para o login
            if (msg.contains("sucesso", ignoreCase = true)) {
                onNavigateToLogin()
                authViewModel.clearFeedBack(0) // Limpa o feedback para não mostrar novamente
            }
            // A exibição do Toast global é tratada no NavHost, mas a navegação é local.
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cadastro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            // ⚠️ Mostra o progresso no centro enquanto carrega
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            // Campos de entrada
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Nome de Exibição") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha (mínimo 6 caracteres)") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.cadastrar(email, password, displayName)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = displayName.isNotBlank() && email.isNotBlank() && password.length >= 6 // ✅ MELHORIA: Validação básica
            ) {
                Text("Cadastrar")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("Já tem uma conta? Faça login")
            }
        }
    }
}