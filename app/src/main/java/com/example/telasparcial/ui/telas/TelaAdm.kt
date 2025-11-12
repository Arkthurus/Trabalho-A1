package com.example.telasparcial.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.telasparcial.viewmodel.PreferencesViewModel

data class UserAdm(
    val uid: String,
    val email: String,
    val isAdm: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdm(
    navController: NavController,
    preferencesViewModel: PreferencesViewModel // 1. Receba a instância do ViewModel
) {
    // 2. Observe o estado do ViewModel. O app irá recompor quando ele mudar.
    val preferencesUiState by preferencesViewModel.uiState.collectAsStateWithLifecycle()

    // Estado local apenas para controlar a visibilidade do seletor de cores
    var mostrarSeletorDeCor by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { mostrarSeletorDeCor = true },
                // 4. Use a cor do botão vinda diretamente do ViewModel
                colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                // 5. Use a cor do texto vinda diretamente do ViewModel
                Text("Cor dos Botões", color = preferencesUiState.corDeTexto)
            }

            // O AlertDialog só é exibido quando 'mostrarSeletorDeCor' é true
            if (mostrarSeletorDeCor) {
                SeletorDeCorRGBDialog(
                    corInicial = preferencesUiState.corDeBotao,
                    onCorSelecionada = { novaCor ->
                        // 6. Notifique o ViewModel que a cor mudou
                        preferencesViewModel.atualizarCorDoBotao(novaCor)
                        mostrarSeletorDeCor = false // Fecha o seletor
                    },
                    onDismiss = {
                        mostrarSeletorDeCor = false // Fecha se o usuário clicar fora
                    }
                )
            }
        }
    }
}

@Composable
fun SeletorDeCorRGBDialog(
    corInicial: Color,
    onCorSelecionada: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var r by remember { mutableFloatStateOf(corInicial.red) }
    var g by remember { mutableFloatStateOf(corInicial.green) }
    var b by remember { mutableFloatStateOf(corInicial.blue) }

    val novaCor = Color(r, g, b)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecione uma Cor") },
        text = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(novaCor)
                )
                Text(text = "Vermelho: ${(r * 255).toInt()}")
                Slider(value = r, onValueChange = { r = it })
                Text(text = "Verde: ${(g * 255).toInt()}")
                Slider(value = g, onValueChange = { g = it })
                Text(text = "Azul: ${(b * 255).toInt()}")
                Slider(value = b, onValueChange = { b = it })
            }
        },
        confirmButton = {
            Button(onClick = { onCorSelecionada(novaCor) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


