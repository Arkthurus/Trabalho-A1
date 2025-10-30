package com.example.myapplication.uiprojeto


import androidx.compose.runtime.LaunchedEffect
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.mvvm.data.UsuarioRepository
import com.example.myapplication.mvvm.data.UsuarioViewModel
import com.example.myapplication.mvvm.data.UsuarioViewModelFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


// -------------------------------------------------------------------------
// COMPOSABLE PARA EXIBIR UM CAMPO NO FORMATO DO TEMA (REINTRODUZIDO)
// -------------------------------------------------------------------------

@Composable
fun CampoInfoTema(label: String, value: String, isLast: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Rótulo (Label) do Campo
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray, // Cinza escuro
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Valor do Campo (Dados do Usuário)
        Text(
            text = value.uppercase(), // Caixa alta
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }
    // Separador (Divider), exceto no último
    if (!isLast) {

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = 0.7f)
        )
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE TELA DE CADASTRO PRINCIPAL (Versão Final com Estado de Carregamento)
// -------------------------------------------------------------------------
@Composable
fun TelaCadastroUsuario(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val usuarioDao = db.usuarioDAO()

    val viewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(
            UsuarioRepository(usuarioDao)
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // var hasLoadedInitialData by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }


    //
    LaunchedEffect(uiState.showSnackbar, uiState.usuarioPrincipal, uiState.isLoading) {

        // Lógica do Snackbar
        if (uiState.showSnackbar) {
            snackbarHostState.showSnackbar(
                message = uiState.snackbarMessage,
                actionLabel = uiState.snackbarAction,
                duration = SnackbarDuration.Short
            )
            viewModel.onSnackbarDismiss()
        }

        /*
         ______________ALTERAÇÃO DO LINCO______________
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.showSnackbar(
                message = uiState.snackbarMessage,
                actionLabel = uiState.snackbarAction,
                duration = SnackbarDuration.Short
            )
            viewModel.onSnackbarDismiss()
        }
        */

        // Lógica para controle do modo de edição/visualização (só após o carregamento)
        if (!uiState.isLoading) {
            if (uiState.usuarioPrincipal == null) {
                isEditing = true // Força edição se não houver usuário
            }
            else if (uiState.snackbarAction == "EDIT" || uiState.snackbarAction == "CREATE") {
                isEditing = false // Volta para visualização após salvar/criar
            }
        }
    }


    // ---------------------- UI ----------------------
    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                SnackbarHost(snackbarHostState) { data ->
                    val containerColor = when (data.visuals.actionLabel) {
                        "EDIT" -> Color(0xFF1976D2) // Azul tema
                        "DELETE" -> Color.Red
                        "CREATE" -> Color(0xFF006400) // Verde
                        else -> MaterialTheme.colorScheme.primary
                    }

                    Snackbar(
                        snackbarData = data,
                        containerColor = containerColor,
                        contentColor = Color.White,
                   )
                }

            }
        }
    ) { innerPadding ->


        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), // Ocupa a tela central
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold // Interrompe a renderização da UI principal
        }

        // ------------------------------------------------------------------
        // UI PRINCIPAL (Só é renderizada se isLoading for FALSE)
        // ------------------------------------------------------------------
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplica o padding do Scaffold
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título Principal
            Text(
                text = if (uiState.usuarioPrincipal == null) "CADASTRO DE USUÁRIO" else "INFORMAÇÕES CADASTRAIS",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp, bottom = 8.dp)
            )

            // Mensagem de Aviso/Contexto
            Text(
                text = when {
                    uiState.usuarioPrincipal == null -> "Preencha os dados abaixo para criar sua conta."
                    isEditing -> "Edite os campos e salve as alterações."
                    else -> "Clique no lápis para editar as informações."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )


            // ------------------------------------------------------------------
            // LÓGICA DE ALTERNÂNCIA DE MODO (Visualização vs Edição)
            // ------------------------------------------------------------------
            if (uiState.usuarioPrincipal == null || isEditing) {
                // MODO EDIÇÃO/CADASTRO
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        TextField(value = uiState.nome, onValueChange = { viewModel.onNomeChange(it) }, label = { Text("Nome Completo") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(value = uiState.email, onValueChange = { viewModel.onEmailChange(it) }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(value = uiState.senha, onValueChange = { viewModel.onSenhaChange(it) }, label = { Text("Senha") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(value = uiState.cpf, onValueChange = { viewModel.onCpfChange(it) }, label = { Text("CPF") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(value = uiState.telefone, onValueChange = { viewModel.onTelefoneChange(it) }, label = { Text("Telefone (Opcional)") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = { viewModel.onSave() }, modifier = Modifier.fillMaxWidth()) {
                            Text(uiState.textoBotao)
                        }

//                        if (uiState.usuarioPrincipal != null && isEditing) {
//                            Spacer(modifier = Modifier.height(10.dp))
//                            TextButton(onClick = { isEditing = false }, modifier = Modifier.fillMaxWidth()) {
//                                Text("Voltar para Visualização")
//                            }
//                        }

                        if (uiState.usuarioPrincipal != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.onDelete() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Deletar Conta")
                            }
                        }
                    }
                }
            }
            // ------------------------------------------------------------------
            // MODO VISUALIZAÇÃO
            // ------------------------------------------------------------------
            else {
                // Conteúdo de Visualização
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        Icons.Default.Edit, "Editar",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isEditing = true }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                uiState.usuarioPrincipal?.let { usuario ->
                    CampoInfoTema(label = "Nome Completo", value = usuario.nome)
                    CampoInfoTema(label = "E-mail", value = usuario.email)
                    CampoInfoTema(label = "CPF", value = usuario.cpf)
                    CampoInfoTema(label = "Telefone", value = usuario.telefone ?: "Não informado", isLast = true)
                }
            }
        }
    }
}