package com.example.myapplication.uiprojeto


import com.example.myapplication.mvvm.data.CronogramaRepository
import com.example.myapplication.mvvm.data.CronogramaViewModel
import com.example.myapplication.mvvm.data.CronogramaViewModelFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.database.entities.Cronograma
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


// -------------------------------------------------------------------------
// COMPOSABLE INDIVIDUAL DO CARD DO DIA
// -------------------------------------------------------------------------

@Composable
fun ItemDiaCronograma(
    dia: Cronograma,
    onEditClick: (Cronograma) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onEditClick(dia) },
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "DIA ${dia.diaDoMes}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (!dia.nome.isNullOrBlank()) {
                    Text(text = "${dia.nome} (${dia.horario ?: ""})", style = MaterialTheme.typography.bodyMedium)
                    dia.descricao?.let { Text(text = it, style = MaterialTheme.typography.bodySmall, maxLines = 1) }
                } else {
                    Text(text = "Nenhum evento agendado", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
        }
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE TELA DO CRONOGRAMA PRINCIPAL (Refatorada para MVVM)
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCronograma(modifier: Modifier = Modifier) {

    // --- 1. Instanciação MVVM ---
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val cronogramaDao = db.cronogramaDAO()

    val viewModel: CronogramaViewModel = viewModel(
        factory = CronogramaViewModelFactory(
            CronogramaRepository(cronogramaDao)
        )
    )

    // Coleta o estado reativo
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // --- 2. Estados Locais da UI ---
    var itemSelecionado by remember { mutableStateOf<Cronograma?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // --- 3. LaunchedEffect para Eventos de Estado ---
    LaunchedEffect(uiState.showSnackbar) {
        if (uiState.showSnackbar) {
            snackbarHostState.showSnackbar(
                message = uiState.snackbarMessage,
                duration = SnackbarDuration.Short
            )
            viewModel.onSnackbarDismiss() // Informa ao ViewModel para esconder o snackbar
        }
    }

    // --- UI PRINCIPAL ---
    Scaffold(
        topBar = { TopAppBar(title = { Text("Agenda Mensal") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        // 4. Tratamento de Carregamento
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // 5. Exibição da Lista
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(uiState.listaCronograma, key = { it.diaDoMes }) { dia ->
                ItemDiaCronograma(
                    dia = dia,
                    onEditClick = {
                        itemSelecionado = it
                        showEditDialog = true
                    }
                )
            }
        }
    }

    // --- DIALOG DE EDIÇÃO (POUP-UP) ---
    if (showEditDialog && itemSelecionado != null) {
        EditarCronogramaDialog(
            itemInicial = itemSelecionado!!,
            onDismiss = { showEditDialog = false },
            onSave = { itemEditado ->

                viewModel.onSave(itemEditado)
                showEditDialog = false // Fecha o dialog
            }
        )
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE DIALOG DE EDIÇÃO
// -------------------------------------------------------------------------

@Composable
fun EditarCronogramaDialog(
    itemInicial: Cronograma,
    onDismiss: () -> Unit,
    onSave: (Cronograma) -> Unit
) {
    var nome by remember { mutableStateOf(itemInicial.nome) }
    var horario by remember { mutableStateOf(itemInicial.horario) }
    var descricao by remember { mutableStateOf(itemInicial.descricao) }

    val itemVazio = itemInicial.copy(nome = null, horario = null, descricao = null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Dia ${itemInicial.diaDoMes}") },
        text = {
            Column {
                TextField(
                    value = nome ?: "",
                    onValueChange = { nome = it },
                    label = { Text("Nome do Evento/Tarefa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = horario ?: "",
                    onValueChange = { horario = it },
                    label = { Text("Horário (Ex: 14:30)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descricao ?: "",
                    onValueChange = { descricao = it },
                    label = { Text("Descrição detalhada") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val itemSalvo = itemInicial.copy(
                        nome = nome?.ifBlank { null },
                        horario = horario?.ifBlank { null },
                        descricao = descricao?.ifBlank { null }
                    )
                    onSave(itemSalvo)
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { onSave(itemVazio) },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Limpar Evento")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}