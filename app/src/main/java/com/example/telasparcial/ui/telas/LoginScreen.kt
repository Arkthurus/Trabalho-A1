package com.example.telasparcial.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.telasparcial.viewmodel.AuthViewModel
import com.example.telasparcial.viewmodel.PreferencesUiState

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    preferencesUiState: PreferencesUiState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by authViewModel.loading.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(preferencesUiState.corDeFundo)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Login",
            style = MaterialTheme.typography.headlineMedium,
            color = preferencesUiState.corDeTexto
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            val colors = TextFieldDefaults.colors(
                focusedTextColor = preferencesUiState.corDeTexto,
                unfocusedTextColor = preferencesUiState.corDeTexto.copy(alpha = .3f),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = colors
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = colors
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.login(email = email, senha = password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() && password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = preferencesUiState.corDeBotao,
                    contentColor = preferencesUiState.corDeTexto
                )
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