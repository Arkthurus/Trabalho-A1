package com.example.telasparcial.ui.telas

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
        }
    }
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