package com.example.telasparcial.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.AlertDialogDefaults
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.telasparcial.viewmodel.PreferencesViewModel
import com.google.firebase.annotations.concurrent.Background

enum class DialogoAtivo {
    COR_BOTOES,
    COR_TEXTO,
    COR_CARTOES,
    COR_FUNDO,
    NENHUM
}

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

    var dialogoAtivo by remember { mutableStateOf(DialogoAtivo.NENHUM) }

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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = preferencesUiState.corDeFundo,
                    titleContentColor = preferencesUiState.corDeTexto
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(preferencesUiState.corDeFundo)
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val buttonModifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .border(
                    1.dp,
                    preferencesUiState.corDeBotao,
                    RoundedCornerShape(36.dp)
                )

            // Diálogo de cor dos botões
            Button(
                onClick = {
                    dialogoAtivo = DialogoAtivo.COR_BOTOES
                },
                colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao),
                modifier = buttonModifier
            ) {
                Text("Cor dos Botões", color = preferencesUiState.corDeTexto)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Diálogo de cor do texto
            Button(
                onClick = {
                    dialogoAtivo = DialogoAtivo.COR_TEXTO
                },
                colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao),
                modifier = buttonModifier
            ) {
                Text("Cor do Texto", color = preferencesUiState.corDeTexto)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Diálogo de cor dos cartões
            Button(
                onClick = {
                    dialogoAtivo = DialogoAtivo.COR_CARTOES
                },
                colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeCards),
                modifier = buttonModifier
            ) {
                Text("Cor dos Cartões", color = preferencesUiState.corDeTexto)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Diálogo de cor do fundo
            Button(
                onClick = {
                    dialogoAtivo = DialogoAtivo.COR_FUNDO
                },
                colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeFundo),
                modifier = buttonModifier
            ) {
                Text("Cor do Fundo", color = preferencesUiState.corDeTexto)
            }
        }
    }

    val corInicial: Color
    val funcaoAlterarCor: (Color) -> Unit

    when (dialogoAtivo) {
        DialogoAtivo.COR_BOTOES -> {
            corInicial = preferencesUiState.corDeBotao
            funcaoAlterarCor = preferencesViewModel::atualizarCorDoBotao
        }

        DialogoAtivo.COR_TEXTO -> {
            corInicial = preferencesUiState.corDeTexto
            funcaoAlterarCor = preferencesViewModel::atualizarCorDoTexto
        }

        DialogoAtivo.COR_CARTOES -> {
            corInicial = preferencesUiState.corDeCards
            funcaoAlterarCor = preferencesViewModel::atualizarCorDosCards
        }

        DialogoAtivo.COR_FUNDO -> {
            corInicial = preferencesUiState.corDeFundo
            funcaoAlterarCor = preferencesViewModel::atualizarCorDoFundo
        }

        DialogoAtivo.NENHUM -> {
            corInicial = Color.Transparent
            funcaoAlterarCor = {}
        }
    }

    if (dialogoAtivo != DialogoAtivo.NENHUM) {
        SeletorDeCorRGBDialog(
            corInicial = corInicial,
            onCorSelecionada = { novaCor ->
                funcaoAlterarCor(novaCor)
                dialogoAtivo = DialogoAtivo.NENHUM
            },
            onDismiss = { dialogoAtivo = DialogoAtivo.NENHUM }
        )
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
            Column(
            ) {
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


