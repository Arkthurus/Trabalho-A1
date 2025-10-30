package com.example.myapplication


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.uiprojeto.Cabecalho
import com.example.myapplication.uiprojeto.Rodape
import com.example.myapplication.uiprojeto.TelaCarteirinha
import com.example.myapplication.uiprojeto.TelaHome
import com.example.myapplication.uiprojeto.TelaOfertas
import com.example.myapplication.uiprojeto.Telaqr
import com.example.myapplication.uiprojeto.TelaCadastroUsuario
import com.example.myapplication.uiprojeto.TelaCronograma

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppScreen() {

    val navController = rememberNavController()

    // 1. Define o mapa de rotas e seus títulos
    val routeToTitleMap = mapOf(
        "home" to "Home",
        "carteirinha" to "Carteirinha",
        "ofertas" to "Benefícios e Ofertas",
        "qrcode" to "QR Code",
        "cadastroUsuario" to "Cadastro de Usuário",
        "cronograma" to "Agenda Mensal"
    )

    // 2. Observe o estado atual da navegação
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // 3. Obtenha a rota atual e defina o título
    // Usa o Map para encontrar o título, ou "Home" como padrão se a rota for nula ou desconhecida.
    val currentRoute = navBackStackEntry?.destination?.route
    val currentTitle = routeToTitleMap[currentRoute] ?: "Home"


    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(top = 0.dp)) {
                // 4. Usa o título dinâmico aqui
                Cabecalho(titulo = currentTitle, mostrarIconeDeRosto = true)
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(bottom = 25.dp)) {
                Rodape(navController = navController)
            }
        }
    ) { innerPadding ->
        // 3. O NavHost gerencia as telas
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // 4. Cada composable define uma tela
            composable("home") {
                TelaHome(navController = navController)
            }
            composable("carteirinha") {
                TelaCarteirinha()
            }
            composable("ofertas") {
                TelaOfertas()
            }
            composable("qrcode") {
                Telaqr()
            }
            composable("cadastroUsuario") {
                TelaCadastroUsuario()
            }
            composable("cronograma") {
                TelaCronograma()
            }
        }
    }
}