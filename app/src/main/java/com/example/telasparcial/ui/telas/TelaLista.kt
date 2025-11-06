package com.example.telasparcial.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.ui.viewmodel.AuthViewModel
import com.example.telasparcial.ui.viewmodel.ContatoViewModel
import com.example.telasparcial.ui.viewmodel.GrupoContatoViewModel
import com.example.telasparcial.ui.viewmodel.GrupoViewModel
import com.google.firebase.auth.FirebaseUser


@Composable
fun TelaLista(
    navController: NavController,
    contatoViewModel: ContatoViewModel,
    grupoViewModel: GrupoViewModel,
    grupoContatoViewModel: GrupoContatoViewModel
) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            item { Spacer(modifier = Modifier.height(5.dp)) }

            // ===============================================
            // ✅ NOVO: BOTÃO DE ACESSO ADMIN TEMPORÁRIO
            // Este botão navega para a rota "TelaAdm"
            // ===============================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { navController.navigate("TelaAdm") },
                        modifier = Modifier.width(150.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.8f), // Destaca para ser temporário
                            contentColor = Color.White
                        )
                    ) {
                        Text("Modo Admin")
                    }
                }
            }
            // ===============================================

            item { Spacer(modifier = Modifier.height(5.dp)) }
            item { FavoriteContacts(navController, contatoViewModel, grupoViewModel, grupoContatoViewModel) }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { RecentContactsList(navController, contatoViewModel, grupoViewModel, grupoContatoViewModel) }
            item { Spacer(modifier = Modifier.height(15.dp)) }
            // OTIMIZAÇÃO: Mantido o forEach/Column, mas com a ressalva de que para listas MUITO grandes,
            // o ideal seria refatorar para LazyColumn de Rows para melhor performance.
            item { DuploCtt(navController, contatoViewModel, grupoViewModel, grupoContatoViewModel) }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteContacts(
    navController: NavController,
    contatoViewModel: ContatoViewModel,
    grupoViewModel: GrupoViewModel,
    grupoContatoViewModel: GrupoContatoViewModel,
) {

    val uiStateGrupoCtt by grupoContatoViewModel.uiState.collectAsStateWithLifecycle()

    val gruposComContatos = uiStateGrupoCtt.gruposComContatos

    val grupoFavoritos = gruposComContatos.find { it.grupo.nome == "Favoritos" }

    val contatos = grupoFavoritos?.contatos ?: emptyList()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .width(420.dp)
            .height(120.dp)
            .padding(start = 22.dp, end = 20.dp, top = 10.dp)
    ) {
        Column {
            Text(
                text = "Favoritos",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
            )
            LazyRow {
                items(contatos) { contato ->
                    Spacer(modifier = Modifier.width(15.dp))
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Contato Favorito: ${contato.nome}",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(start = 10.dp, bottom = 10.dp)
                            .combinedClickable(
                                // ✅ CORREÇÃO AQUI: Chamar a função de remoção do grupo
                                onDoubleClick = {
                                    // 1. Busca o grupo "Favoritos" (pode ser null se não existir)
                                    val grupoFavoritosParaRemover = grupoFavoritos?.grupo

                                    // 2. Chama a função que remove APENAS a ligação (GrupoContato)
                                    if (grupoFavoritosParaRemover != null) {
                                        grupoContatoViewModel.removerDoGrupo(
                                            grupoFavoritosParaRemover,
                                            contato
                                        )
                                    }
                                },
                                onClick = {
                                    contatoViewModel.receberCttEdit(contato)
                                    navController.navigate("TelaEdit")
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
        }
    }
}


@Composable
private fun RecentContactsList(
    navController: NavController,
    contatoViewModel: ContatoViewModel,
    grupoViewModel: GrupoViewModel,
    grupoContatoViewModel: GrupoContatoViewModel
) {
    val uiState by contatoViewModel.uiState.collectAsStateWithLifecycle()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = Modifier.size(width = 400.dp, height = 270.dp)
    ) {
        Text(
            text = "Recentes",
            modifier = Modifier.padding(start = 35.dp),
            textAlign = TextAlign.Center,
        )
        LazyColumn {
            items(uiState.lista4Contatos) { contato ->
                RecentContactCard(
                    navController,
                    contato,
                    contatoViewModel,
                    grupoViewModel,
                    grupoContatoViewModel
                )
            }
        }
    }
}

@Composable
fun DuploCtt(
    navController: NavController,
    contatoViewModel: ContatoViewModel,
    grupoViewModel: GrupoViewModel,
    grupoContatoViewModel: GrupoContatoViewModel
) {

    val uiState by contatoViewModel.uiState.collectAsStateWithLifecycle()

    Column {
        // Usa `chunked(2)` para agrupar em pares
        uiState.listaDeContatos.chunked(2).forEach { parDeContatos ->
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                parDeContatos.forEach { contato ->
                    ContactCard(contato, navController, contatoViewModel, grupoViewModel, grupoContatoViewModel)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun ContactCard(
    contato: Contato,
    navController: NavController,
    contatoViewModel: ContatoViewModel,
    grupoViewModel: GrupoViewModel,
    grupoContatoViewModel: GrupoContatoViewModel
) {
    // Lógica de I/O removida, agora a responsabilidade é do ViewModel

    Spacer(modifier = Modifier.width(20.dp))
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .size(width = 190.dp, height = 170.dp)

    ) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween){
            Row {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Adicionar aos Favoritos",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 5.dp, top = 5.dp)
                        .clickable {
                            //Delega a lógica de I/O para o ViewModel
                            contatoViewModel.adicionarAosFavoritos(
                                contato,
                                grupoViewModel,
                                grupoContatoViewModel
                            )
                        }
                )
                Text(
                    text = contato.nome,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                )
            }
            Row {
                Text(
                    text = contato.numero,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }
            Row {
                //Editar
                Button(
                    onClick = {
                        contatoViewModel.receberCttEdit(contato)
                        navController.navigate("TelaEdit") },
                    modifier = Modifier
                        .width(95.dp)
                        .padding( 10.dp),
                    shape = ButtonDefaults.filledTonalShape
                ) {
                    Icon(
                        Icons.Default.Create,
                        contentDescription = "Editar Contato",
                        modifier = Modifier.size(20.dp)
                    )
                }
                //Deletar
                Button(
                    onClick = {
                        contatoViewModel.deletarContato(contato)
                    },
                    modifier = Modifier
                        .width(95.dp)
                        .padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
                    shape = ButtonDefaults.filledTonalShape
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Deletar Contato"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentContactCard(
    navController: NavController,
    contato: Contato,
    contatoViewModel: ContatoViewModel,
    grupoViewModel: GrupoViewModel,
    grupoContatoViewModel: GrupoContatoViewModel
) {
    // Lógica de I/O removida, agora a responsabilidade é do ViewModel

    Column(modifier = Modifier.padding()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 5.dp, bottom = 5.dp, start = 22.dp)
                .border(
                    shape = CircleShape,
                    border = BorderStroke(5.dp, color = Color.LightGray)
                )
                .combinedClickable(
                    onClick = {
                        contatoViewModel.receberCttEdit(contato)
                        navController.navigate("TelaEdit")
                    },
                    onLongClick = {
                        contatoViewModel.deletarContato(contato)
                    },
                    onDoubleClick = { // Adicionar aos favoritos
                        // ✅ CHAMADA OTIMIZADA: Delega a lógica de I/O para o ViewModel
                        contatoViewModel.adicionarAosFavoritos(
                            contato,
                            grupoViewModel,
                            grupoContatoViewModel
                        )
                    }
                )
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .background(shape = CircleShape, color = Color.LightGray)
                    .padding(start = 5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Ícone de Perfil",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(top = 10.dp)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = contato.nome,
                        modifier = Modifier.padding(bottom = 15.dp, top = 10.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}