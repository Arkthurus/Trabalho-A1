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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.telasparcial.data.entities.Contato
import com.example.telasparcial.viewmodel.ContatoViewModel
import com.example.telasparcial.viewmodel.PreferencesUiState

@Composable
fun TelaEdit(
    navController: NavController,
    contatoViewModel: ContatoViewModel,
    preferencesUiState: PreferencesUiState,
) {

    val uiState by contatoViewModel.uiState.collectAsStateWithLifecycle()

    val contatoEditar = uiState.contatoEmEdit

    var nome by remember { mutableStateOf(contatoEditar?.nome ?: "") }
    var numeroTelefone by remember { mutableStateOf(contatoEditar?.numero ?: "") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Modificar Contato", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = numeroTelefone,
            onValueChange = { numeroTelefone = it },
            label = { Text("Número de Telefone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            readOnly = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {

                val contatoEditado = Contato(
                    nome = nome,
                    numero = numeroTelefone,
                    id = contatoEditar?.id ?: 0
                )
                contatoViewModel.atualizarContato(contato = contatoEditado)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Alterações")
        }
    }
}