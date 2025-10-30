package com.example.telasparcial.ui.telas

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TelaDiscagem(navController: NavController, onNavigateToAddCtt: (String) -> Unit) {
    Scaffold(
        bottomBar = {BottomBar(navController)}
    ) {
        Column(modifier = Modifier.padding(top = 50.dp)
        ) {
            Discagem(onNavigateToAddCtt)
        }
    }
}

@Composable
fun BottomBar(navController : NavController) {
    Surface(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth(),
        color = Color.LightGray
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Buttons da sua bottom bar
            BottomButton(Icons.Default.Menu, onClick = {navController.navigate("TelaLista")})
            BottomButton(Icons.Default.Call, onClick = {navController.navigate("TelaDiscar")})
            BottomButton(Icons.Default.AccountCircle, onClick = {navController.navigate("TabScreen")})
        }
    }
}
@Composable
fun Discagem(onNavigateToAddCtt: (String) -> Unit) {
    // Estado que guarda o número digitado
    var phoneNumber by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.padding(16.dp),
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
                    NumberButton("1", onClick = { phoneNumber += "1" })
                    NumberButton("2", onClick = { phoneNumber += "2" })
                    NumberButton("3", onClick = { phoneNumber += "3" })
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberButton("4", onClick = { phoneNumber += "4" })
                    NumberButton("5", onClick = { phoneNumber += "5" })
                    NumberButton("6", onClick = { phoneNumber += "6" })
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberButton("7", onClick = { phoneNumber += "7" })
                    NumberButton("8", onClick = { phoneNumber += "8" })
                    NumberButton("9", onClick = { phoneNumber += "9" })
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberButton("*", onClick = { phoneNumber += "*" })
                    NumberButton("0", onClick = { phoneNumber += "0" })
                    NumberButton("#", onClick = { phoneNumber += "#" })
                }
                Spacer(modifier = Modifier.height(40.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CallActionButton(
                        icon = Icons.Default.Call,
                        contentDescription = "Ligar",
                        onClick = { /* Ação do botão Ligar */ }
                    )
                    Spacer(modifier = Modifier.width(70.dp))
                    CallActionButton(
                        icon = Icons.Default.AddCircle,
                        contentDescription = "Adicionar Contato",
                        onClick = {
                            // Chama a função de navegação, passando o número digitado
                            onNavigateToAddCtt(phoneNumber)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NumberButton(digit: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(110.dp)
    ) {
        Text(digit, fontSize = 32.sp)
    }
}

@Composable
fun CallActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
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