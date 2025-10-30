package com.example.telasparcial.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp


@Composable
@Preview
fun UserCard(modifier: Modifier = Modifier) {
    Surface(tonalElevation = 4.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Your profile picture.",
                tint = Color.DarkGray
            )

            Column {
                Text(
                    "Nome de usuário",
                    fontSize = TextUnit(5f, TextUnitType.Em)
                )

                Text(
                    "(99) 99999-9999",
                    fontSize = TextUnit(3f, TextUnitType.Em)
                )
            }
        }
    }

}

