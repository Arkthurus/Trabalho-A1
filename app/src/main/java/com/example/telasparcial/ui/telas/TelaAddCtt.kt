package com.example.telasparcial.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
// import androidx.lifecycle.compose.collectAsStateWithLifecycle // Import removido
import androidx.navigation.NavHostController
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.ui.viewmodel.ContatoViewModel


@Composable
fun AddCtt(
    numeroCtt: String,

    contatoViewModel: ContatoViewModel,
    navController: NavHostController
) {
    // Estado para o campo de nome
    var name by remember { mutableStateOf("") }
    // O número de telefone é passado como um parâmetro
    val phoneNumber by remember { mutableStateOf(numeroCtt) }

    // ✅ OTIMIZAÇÃO: Removida a coleta de uiStateCtt, pois não é usada na tela.
    // val uiStateCtt by contatoViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Adicionar Novo Contato", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // Campo para o nome do contato
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para o número de telefone, desabilitado para edição
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {}, // Não permite edição
            label = { Text("Número de Telefone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            readOnly = true, // Torna o campo somente leitura
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(32.dp))

        // Botão para salvar
        Button(
            onClick = {
                val contatoAdd = Contato(nome = name, numero = phoneNumber)
                contatoViewModel.salvarContato(contatoAdd)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            // ✅ MELHORIA: Só permite salvar se o nome não estiver vazio/em branco
            enabled = name.isNotBlank()
        ) {
            Text("Salvar Contato")
        }
    }
}