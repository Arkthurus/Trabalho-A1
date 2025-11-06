package com.example.telasparcial.ui.telas

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telasparcial.R
import com.example.telasparcial.ui.viewmodel.AdviceViewModel
import com.example.telasparcial.ui.viewmodel.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen(navController: NavController, authViewModel: AuthViewModel) {
    val tabNavController = rememberNavController()
    var selectedTabIndex by remember { mutableStateOf(0) }
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) {
        Column {
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = {
                        selectedTabIndex = 0
                        tabNavController.navigate("meucodigo")
                    },
                    text = { Text("Meu código") }
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = {
                        selectedTabIndex = 1
                        tabNavController.navigate("escanearcodigo")
                    },
                    text = { Text("Escanear Código") }
                )
            }

            NavHost(navController = tabNavController, startDestination = "meucodigo") {
                composable("meucodigo") {
                    // A tela de QR Code agora precisa de um parâmetro (ex: o número de telefone)
                    // Se o número de telefone vier de uma tela anterior, ele deve ser
                    // passado para a TabScreen e depois para TelaQR
                    TelaQR(navController, authViewModel)
                }
                composable("escanearcodigo") { TelaEscanearCodigo() }
            }
        }
    }
}


@Composable
fun TelaQR(navController: NavController, authViewModel: AuthViewModel) {

    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(Modifier.height(10.dp))
            ProfileStats(navController, authViewModel)
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(painter = painterResource(id = R.drawable.qr_code), contentDescription = null, modifier = Modifier.size(200.dp))
            }

            val viewModel: AdviceViewModel = viewModel()
            val advice = viewModel.advice.value

            Text(text = advice,
                style = androidx.compose.material.MaterialTheme.typography.h3,
                fontSize = TextUnit(value = 5f, type = TextUnitType.Em),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp).height(100.dp))

            Box(
                modifier = Modifier
                    .size(200.dp)

            ) {

                Button(
                    onClick = { viewModel.fetchAdvice()},
                    modifier = Modifier
                        .fillMaxWidth(.85f)
                        .height(50.dp)
                        .align(Alignment.TopCenter)
                        .padding(top = 5.dp)
                ) {
                    Text("Novo Conselho")
                }

                Button(onClick = {
                    navController.navigate("TelaEditUSER")
                },
                    modifier = Modifier
                        .fillMaxWidth(.85f)
                        .height(50.dp)
                        .align(Alignment.Center)

                ){
                    Text("Editar User",
                        fontSize = TextUnit(value = 4.5f, TextUnitType.Em))
                }
                Button(
                    onClick = {
                        authViewModel.desLogar()
                    },
                    modifier = Modifier
                        .fillMaxWidth(.85f)
                        .height(50.dp)
                        .align(Alignment.BottomCenter)

                ) {
                    Text("Deslogar",
                        fontSize = TextUnit(value = 4.5f, TextUnitType.Em)
                    )
                }
            }
        }
    }
}


@Composable
fun TelaEscanearCodigo() {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp)
        ) {
            Icon(Icons.Default.Share, "Icone share")
            Text("Aponte sua câmera para o código QR de alguém!")
        }

        // Image by Daniel Harntanto: https://www.vecteezy.com/vector-art/9293275-qr-code-vector-for-website-symbol-icon-presentation
        Image(
            painter = painterResource(
                R.drawable.vecteezy_qr_code_vector_for_website_symbol_icon_presentation_9293275
            ),
            "Scan QR Image"
        )

        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Código escaneado! (Dummy)",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth(.85f)
                .padding(top = 100.dp)
                .height(80.dp)
        ) {
            Text("Escanear Código", style = MaterialTheme.typography.headlineSmall)
        }
    }
}


@Composable
fun ProfileStats(navController: NavController, authViewModel: AuthViewModel) {

    // 1. Coletar o estado do usuário e de loading do ViewModel
    val userState by authViewModel.userState.collectAsStateWithLifecycle()
    val isLoading by authViewModel.loading.collectAsStateWithLifecycle()

    val requiresReAuth by authViewModel.requiresReAuth.collectAsStateWithLifecycle()

    // Variáveis de estado para os campos de texto
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val context = LocalContext.current

    var showReAuthDialog by remember { mutableStateOf(false) }
    var senhaAtual by remember { mutableStateOf("") }

    LaunchedEffect(requiresReAuth) {
        if (requiresReAuth){
            showReAuthDialog = true
        }
    }

    // 2. LaunchedEffect para preencher os campos quando a tela carregar
    // Ele será executado sempre que 'userState' mudar.
    LaunchedEffect(userState) {
        userState?.email?.let { currentUserEmail ->
            email = currentUserEmail
        }
    }

    // 3. LaunchedEffect para observar o feedback (sucesso/erro)
    LaunchedEffect(Unit) {
        authViewModel.authFeedback.collect { feedback ->
            feedback?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                // Se a atualização foi bem-sucedida, navega de volta
                if (it.contains("sucesso", ignoreCase = true)) {
                    navController.popBackStack()
                }
                authViewModel.clearFeedBack() // Limpa o feedback para não mostrar de novo
            }
        }
    }
    val emailShow = userState?.email
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Image(
            Icons.Default.AccountCircle,
            "User Icon",
            modifier = Modifier
                .size(100.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "$emailShow",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}