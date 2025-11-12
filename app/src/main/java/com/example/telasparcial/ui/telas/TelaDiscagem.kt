package com.example.telasparcial.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.graphics.Color
import com.example.telasparcial.viewmodel.PreferencesUiState

// Removida a anotação @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
// pois usaremos o parâmetro padding
@Composable
fun TelaDiscagem(
    navController: NavController,
    onNavigateToAddCtt: (String) -> Unit,
    preferencesUiState: PreferencesUiState
) {
    Scaffold(
        bottomBar = { BottomBar(navController, preferencesUiState)},
        modifier = Modifier.background(color = preferencesUiState.corDeFundo),
        containerColor = preferencesUiState.corDeFundo
    ) { innerPadding -> // ✅ Recebe o padding do Scaffold
        Column(
            // ✅ CORREÇÃO: Aplica o padding interno, especialmente o inferior
            modifier = Modifier.padding(innerPadding).background(color = preferencesUiState.corDeFundo)
        ) {
            Discagem(onNavigateToAddCtt, preferencesUiState)
        }
    }
}

// 🎯 MELHORIA: Usando componentes padrão do Material3 para a NavigationBar
@Composable
fun BottomBar(navController: NavController, preferencesUiState: PreferencesUiState) {
    val currentRoute = remember { mutableStateOf("TelaDiscar") } // Estado básico para controle visual

    // Definição simples dos itens de navegação
    val items = listOf(
        Pair(Icons.Default.Menu, "Lista") to "TelaLista",
        Pair(Icons.Default.Call, "Discar") to "TelaDiscar",
        Pair(Icons.Default.AccountCircle, "Perfil") to "TabScreen"
    )

    NavigationBar(containerColor = Color.DarkGray,
                  contentColor = preferencesUiState.corDeBotao){
        items.forEach { (iconPair, route) ->
            val (icon, label) = iconPair
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label, modifier = Modifier.background(preferencesUiState.corDeBotao, shape = CircleShape).width(70.dp).height(35.dp)) },
                label = { Text(label, color = preferencesUiState.corDeTexto) },
                selected = false, // Verifica se a rota está selecionada
                onClick = {
                    currentRoute.value = route
                    navController.navigate(route) {
                        // Evita empilhar destinos
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        // Evita múltiplas cópias da mesma tela no topo
                        launchSingleTop = true
                        // Restaura estado quando volta
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun Discagem(onNavigateToAddCtt: (String) -> Unit, preferencesUiState: PreferencesUiState) {
    // Estado que guarda o número digitado
    var phoneNumber by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.padding(16.dp),
        color = preferencesUiState.corDeFundo
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = phoneNumber,
                    color = preferencesUiState.corDeTexto,
                    fontSize = 28.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    textAlign = TextAlign.End
                )

                IconButton(
                    onClick = {
                        if (phoneNumber.isNotEmpty()) {
                            phoneNumber = phoneNumber.dropLast(1)
                        }
                    },
                    modifier = Modifier.size(58.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Apagar"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberButton("1", onClick = { phoneNumber += "1" }, preferencesUiState)
                    NumberButton("2", onClick = { phoneNumber += "2" }, preferencesUiState)
                    NumberButton("3", onClick = { phoneNumber += "3" }, preferencesUiState)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberButton("4", onClick = { phoneNumber += "4" }, preferencesUiState)
                    NumberButton("5", onClick = { phoneNumber += "5" }, preferencesUiState)
                    NumberButton("6", onClick = { phoneNumber += "6" }, preferencesUiState)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberButton("7", onClick = { phoneNumber += "7" }, preferencesUiState)
                    NumberButton("8", onClick = { phoneNumber += "8" }, preferencesUiState)
                    NumberButton("9", onClick = { phoneNumber += "9" }, preferencesUiState)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberButton("*", onClick = { phoneNumber += "*" }, preferencesUiState)
                    NumberButton("0", onClick = { phoneNumber += "0" }, preferencesUiState)
                    NumberButton("#", onClick = { phoneNumber += "#" }, preferencesUiState)
                }
                Spacer(modifier = Modifier.height(40.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Botão Ligar (apenas simulação)
                    CallActionButton(
                        icon = Icons.Default.Call,
                        contentDescription = "Ligar",
                        onClick = {
                            // Ação: Iniciar discagem. Adicione lógica aqui se necessário.
                        },
                        modifier = Modifier.background(preferencesUiState.corDeBotao, shape = CircleShape).width(70.dp).height(35.dp)
                    )
                    Spacer(modifier = Modifier.width(70.dp))
                    CallActionButton(
                        icon = Icons.Default.AddCircle,
                        contentDescription = "Adicionar Contato",
                        onClick = {
                            // Verifica se há algo para adicionar antes de navegar
                            if (phoneNumber.isNotBlank()) {
                                onNavigateToAddCtt(phoneNumber)
                            }
                        },
                        modifier = Modifier.background(preferencesUiState.corDeBotao, shape = CircleShape).width(70.dp).height(35.dp)
                    )
                }
            }
        }
    }
}

// Componentes auxiliares inalterados
@Composable
fun NumberButton(digit: String, onClick: () -> Unit, preferencesUiState: PreferencesUiState) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(110.dp),
        colors = ButtonDefaults.buttonColors(containerColor = preferencesUiState.corDeBotao)
    ) {
        Text(digit, fontSize = preferencesUiState.tamanhoDeFonte.tamanho, color = preferencesUiState.corDeTexto)
    }
}

@Composable
fun CallActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(64.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp)
        )
    }
}