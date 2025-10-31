package com.example.telasparcial.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun UserCard(
    nome: String,
    telefone: String,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            // Adicionando padding para melhor espaçamento
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil do usuário",
                tint = Color.DarkGray
            )

            // Espaçador entre o ícone e o texto
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = nome,
                    // Usando tipografia do tema para acessibilidade
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = telefone,
                    // Usando tipografia do tema
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray // Deixando o número um pouco mais discreto
                )
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun UserCardPreview() {
    // Agora o Preview é funcional, pois passamos dados
    UserCard(
        nome = "Nome de usuário",
        telefone = "(99) 99999-9999"
    )
}