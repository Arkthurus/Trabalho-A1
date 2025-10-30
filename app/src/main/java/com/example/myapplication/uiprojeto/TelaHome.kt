package com.example.myapplication.uiprojeto

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun TelaHome(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SecaoCentralH(navController = navController)
        }
    }
}

@Composable
fun SecaoCentralH(modifier: Modifier = Modifier, navController: NavController) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.logoup),
            contentDescription = "Logo da aplicação",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 350.dp)
        )
        Spacer(modifier = Modifier.height(100.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardSecao(
                texto = "Carteirinha",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFFD80E0E),
                imagemResId = R.drawable.celularcard,
                onClick = {
                    navController.navigate("carteirinha")
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            CardSecao(
                texto = "Qr Code",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFF08259D),
                imagemResId = R.drawable.qrcode,
                onClick = {
                    navController.navigate("qrcode")
                }
            )
        }
        Spacer(modifier = Modifier.height(9.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardSecao(
                texto = "Cronograma",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFFEAB505),
                imagemResId = R.drawable.identidade,
                onClick = {
                    navController.navigate("cronograma")
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            CardSecao(
                texto = "Bonus & Ofertas",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFF1D9E99),
                imagemResId = R.drawable.presente,
                onClick = {
                    navController.navigate("ofertas")
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardSecao(
                texto = "Info. Usuário",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFF7C33A8),
                imagemResId = R.drawable.user,
                onClick = {
                    navController.navigate("cadastroUsuario")
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            CardSecao(
                texto = "Camera",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFFFF4500),
                imagemResId = R.drawable.camera,
                onClick = {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    context.startActivity(intent)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            CardSecao(
                texto = "Notificações",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFF16703C),
                imagemResId = R.drawable.sino,
                onClick = {
                    Toast.makeText(context, "Você não possui notificações!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

