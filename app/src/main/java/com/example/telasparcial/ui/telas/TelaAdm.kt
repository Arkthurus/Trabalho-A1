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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// >> NOVAS IMPORTAÇÕES NECESSÁRIAS
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.telasparcial.ui.state.PreferencesUiState

data class UserAdm(
    val uid: String,
    val email: String,
    val isAdm: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAdm(
    navController: NavController
) {
    // Estados para controlar se os interruptores estão ligados ou desligados
    val temaEscuroSistema = isSystemInDarkTheme()
    var temaEscuroAtivado by remember { mutableStateOf(temaEscuroSistema) }
    var preferencesUiState by remember { mutableStateOf(PreferencesUiState()) }


    Scaffold(
        topBar = {
            // Barra superior com título e botão de voltar
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
        // Coluna com rolagem para o conteúdo da tela
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Adiciona a barra de rolagem
        ) {
            // Item de configuração para tema escuro
            ConfiguracaoItem(
                titulo = "Tema Escuro",
                descricao = "Reduza o brilho e melhore a visualização à noite.",
                ativado = temaEscuroAtivado,
                onCheckedChange = { temaEscuroAtivado = it }
            )
            // 1. Estado para armazenar a cor atual do botão.
            var corDoBotao by remember { mutableStateOf(Color(0xFF6200EE)) }
            // 2. Estado para controlar a visibilidade do seletor de cores (AlertDialog).
            var mostrarSeletorDeCor by remember { mutableStateOf(false) }
            Button(
                onClick = {mostrarSeletorDeCor = true},
                colors = ButtonDefaults.buttonColors(containerColor = corDoBotao),
                modifier = Modifier
                    .padding(start = 10.dp, top = 40.dp)
                    .fillMaxWidth()
                    .height(70.dp)
            ) {Text("Cor dos Botões", color = preferencesUiState.corDeTexto!!)}//n vai ser nulo

            //Exemplo de implementação no resto do code
            Button(onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao?: Color(255,255,255))) { }
            //fim do Exemplo

            // 3. Se `mostrarSeletorDeCor` for verdadeiro, o AlertDialog é exibido.
            if (mostrarSeletorDeCor) {
                SeletorDeCorRGBDialog(
                    corInicial = corDoBotao,
                    onCorSelecionada = { novaCor ->
                        preferencesUiState = preferencesUiState.copy(corDeBotao = novaCor)
                        corDoBotao = novaCor // Atualiza a cor do botão.
                        mostrarSeletorDeCor = false // Fecha o seletor.
                    },
                    onDismiss = {
                        mostrarSeletorDeCor = false // Fecha o seletor se o usuário clicar fora.
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
    // Estados para cada canal de cor (0.0f a 1.0f).
    var r by remember { mutableStateOf(corInicial.red) }
    var g by remember { mutableStateOf(corInicial.green) }
    var b by remember { mutableStateOf(corInicial.blue) }

    val novaCor = Color(r, g, b)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecione uma Cor") },
        text = {
            Column {
                // Pré-visualização da cor.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp)) // Arredonda as bordas
                        .background(novaCor) // Aplica a cor de fundo
                )

                // Slider para Vermelho (Red)
                Text(text = "Vermelho: ${(r * 255).toInt()}")
                Slider(value = r, onValueChange = { r = it })

                // Slider para Verde (Green)
                Text(text = "Verde: ${(g * 255).toInt()}")
                Slider(value = g, onValueChange = { g = it })

                // Slider para Azul (Blue)
                Text(text = "Azul: ${(b * 255).toInt()}")
                Slider(value = b, onValueChange = { b = it })
            }
        },
        confirmButton = {
            Button(
                onClick = { onCorSelecionada(novaCor) }
            ) {
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
        Column(
            modifier = Modifier.weight(1f)
        ) {
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