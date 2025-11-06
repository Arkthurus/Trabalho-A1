package com.example.telasparcial.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.telasparcial.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit // Função para navegar para a tela de cadastro
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 1. Coletar o estado de loading
    val isLoading by authViewModel.loading.collectAsStateWithLifecycle()
    // 2. Coletar o estado do usuário para navegação
    val userState by authViewModel.userState.collectAsStateWithLifecycle()

    // 3. Efeito colateral para navegar após login bem-sucedido
    LaunchedEffect(userState) {
        if (userState != null) {
            // A navegação real para a TelaLista será tratada no NavHost,
            // mas aqui podemos garantir que se o usuário for não-nulo, saímos da tela de Login.
            // Para projetos maiores, o ideal é usar um evento de navegação.
            // Aqui confiamos que o NavHost fará a transição corretamente.
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            // ⚠️ Mostra o progresso no centro enquanto carrega
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            // Campos e botões são mostrados apenas quando não está carregando

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
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.login(email = email, senha = password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Entrar")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToSignUp) {
                Text("Não tem uma conta? Cadastre-se")
            }
        }
    }
}