package com.example.telasparcial.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Determina o estado inicial do switch do tema
    val temaSwitch = preferencesUiState.isTemaEscuro ?: isSystemInDarkTheme()

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
            ConfiguracaoItem(
                titulo = "Tema Escuro",
                descricao = "Reduza o brilho e melhore a visualização à noite.",
                ativado = temaSwitch,
                onCheckedChange = { novoEstado ->
                    // 3. Notifique o ViewModel sobre a mudança
                    preferencesViewModel.atualizarTemaEscuro(novoEstado)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { mostrarSeletorDeCor = true },
                // 4. Use a cor do botão vinda diretamente do ViewModel
                colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao!!),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                // 5. Use a cor do texto vinda diretamente do ViewModel
                Text("Cor dos Botões", color = preferencesUiState.corDeTexto!!)
            }

            // O AlertDialog só é exibido quando 'mostrarSeletorDeCor' é true
            if (mostrarSeletorDeCor) {
                SeletorDeCorRGBDialog(
                    corInicial = preferencesUiState.corDeBotao!!,
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
    var r by remember { mutableStateOf(corInicial.red) }
    var g by remember { mutableStateOf(corInicial.green) }
    var b by remember { mutableStateOf(corInicial.blue) }

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

@Composable
fun ConfiguracaoItem(
    titulo: String,
    descricao: String,
    ativado: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = descricao,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Switch(
            checked = ativado,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
