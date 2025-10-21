package com.example.telasparcial.ui.nav

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.telasparcial.data.AppDatabase
import com.example.telasparcial.data.repository.ContatosRepository
import com.example.telasparcial.data.repository.GrupoContatoRepository
import com.example.telasparcial.data.repository.GrupoRepository
import com.example.telasparcial.ui.telas.AddCtt
import com.example.telasparcial.ui.telas.TabScreen
import com.example.telasparcial.ui.telas.TelaDiscagem
import com.example.telasparcial.ui.telas.TelaEdit
import com.example.telasparcial.ui.telas.TelaEscanearCodigo
import com.example.telasparcial.ui.telas.TelaLista
import com.example.telasparcial.ui.telas.TelaQR
import com.example.telasparcial.ui.viewmodel.AuthViewModel
import com.example.telasparcial.ui.viewmodel.ContatoViewModel
import com.example.telasparcial.ui.viewmodel.ContatosViewModelFactory
import com.example.telasparcial.ui.viewmodel.GrupoContatoViewModel
import com.example.telasparcial.ui.viewmodel.GrupoContatoViewModelFactory
import com.example.telasparcial.ui.viewmodel.GrupoViewModel
import com.example.telasparcial.ui.viewmodel.GrupoViewModelFactory
import com.example.telasparcial.ui.telas.SignUpScreen
import com.example.telasparcial.ui.telas.LoginScreen

@Composable
fun AppNav(authViewModel: AuthViewModel) {

    val navController = rememberNavController()

    val user by authViewModel.userState.collectAsStateWithLifecycle()
    val isLoading by authViewModel.loading.collectAsStateWithLifecycle()
    val feedbackMsg by authViewModel.authFeedback.collectAsStateWithLifecycle()

    val applicationContext = LocalContext.current.applicationContext

    LaunchedEffect(feedbackMsg){
        feedbackMsg?.let {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            authViewModel.clearFeedBack(0)
        }
    }

    val contatoViewModel: ContatoViewModel = viewModel(
        factory = ContatosViewModelFactory(
            ContatosRepository(AppDatabase.getDatabase(LocalContext.current).contatosDao())
        )
    )
    val grupoViewModel: GrupoViewModel = viewModel(
    factory = GrupoViewModelFactory(
        GrupoRepository(AppDatabase.getDatabase(LocalContext.current).grupoDao())
    )
    )
    val grupoContatoViewModel: GrupoContatoViewModel = viewModel(
    factory = GrupoContatoViewModelFactory(
        GrupoContatoRepository(
            AppDatabase.getDatabase(LocalContext.current).grupoContatoDao()
        )
    )
    )

    val uiStateCtt by contatoViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = if (user != null) "TelaLista" else "TelaLogin") {
        composable("TelaCadastro"){

            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {navController.navigate("TelaLogin")}
            )
        }
        composable("TelaLogin"){
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate("TelaCadastro") }
            )
        }
        composable("Perfil"){
            if (user != null){
                TelaLista(
                    navController = navController,
                    contatoViewModel = contatoViewModel,
                    grupoViewModel = grupoViewModel,
                    grupoContatoViewModel = grupoContatoViewModel,
                    authViewModel = authViewModel,
                    user = user!!
                )
            }else{
                LaunchedEffect(Unit) {
                    navController.navigate("TelaLogin"){
                        popUpTo(navController.graph.id){inclusive = true}
                    }
                }
            }
        }
        composable("TelaLista") {
            // Passa o navController para a tela principal
            TelaLista(
                navController,
                contatoViewModel,
                grupoViewModel,
                grupoContatoViewModel,
                authViewModel,
                user!!
            )
        }
        composable(
            route = "TelaEdit",
        ) { backStackEntry ->
            TelaEdit(
                navController = navController,
                contatoViewModel = contatoViewModel
            )
        }
        composable("TelaDiscar") {
            TelaDiscagem(
                navController = navController,
                onNavigateToAddCtt = { numeroCtt: String ->
                    navController.navigate("TelaAddCtt/$numeroCtt")
                }
            )
        }
        composable("TabScreen") {
            TabScreen(navController)
        }
        composable(
            route = "TelaAddCtt/{numeroCtt}",
            arguments = listOf(navArgument("numeroCtt") { type = NavType.StringType })
        ) { backStackEntry ->
            val numeroCtt = backStackEntry.arguments?.getString("numeroCtt") ?: ""

            AddCtt(
                //Manter isso
                numeroCtt = numeroCtt,
                contatoViewModel,
                navController
            )
        }
        composable("meuCodigo") { TelaQR() }
        composable("escanearCodigo") { TelaEscanearCodigo() }
    }
}
