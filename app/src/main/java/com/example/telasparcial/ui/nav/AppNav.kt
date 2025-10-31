package com.example.telasparcial.ui.nav

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
import com.example.telasparcial.ui.telas.EditUSER
import com.example.telasparcial.ui.telas.TabScreen
import com.example.telasparcial.ui.telas.TelaDiscagem
import com.example.telasparcial.ui.telas.TelaEdit
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
import com.example.telasparcial.ui.telas.TelaEscanearCodigo

@Composable
fun AppNav(authViewModel: AuthViewModel) {

    val navController = rememberNavController()

    // ✅ Coleta de estados do AuthViewModel
    val user by authViewModel.userState.collectAsStateWithLifecycle()
    val feedbackMsg by authViewModel.authFeedback.collectAsStateWithLifecycle()

    val applicationContext = LocalContext.current.applicationContext

    // ✅ Tratamento global de feedback (Toasts)
    LaunchedEffect(feedbackMsg){
        feedbackMsg?.let {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            authViewModel.clearFeedBack(0)
        }
    }

    // ✅ Injeção de dependência dos ViewModels de persistência (Room)
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

    // A coleta de uiStateCtt não é usada no NavHost e foi removida para simplificar.
    // val uiStateCtt by contatoViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        // ✅ Determinação da tela inicial baseada no estado de autenticação
        startDestination = if (user != null) "TelaLista" else "TelaLogin"
    ) {
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

        // ✅ Rota "Perfil" (usando o mesmo Composable da TelaLista para re-autenticação)
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
                // Redirecionamento seguro para login em caso de deslogar
                LaunchedEffect(Unit) {
                    navController.navigate("TelaLogin"){
                        popUpTo(navController.graph.id){inclusive = true}
                    }
                }
            }
        }

        composable("TelaLista") {
            TelaLista(
                navController,
                contatoViewModel,
                grupoViewModel,
                grupoContatoViewModel,
                authViewModel,
                user!! // Garante que o usuário não é nulo antes de passar
            )
        }
        composable(route = "TelaEdit") {
            TelaEdit(
                navController = navController,
                contatoViewModel = contatoViewModel
            )
        }
        composable(route = "TelaEditUSER") {
            EditUSER(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("TelaDiscar") {
            TelaDiscagem(
                navController = navController,
                onNavigateToAddCtt = { numeroCtt: String ->
                    // ✅ Navegação com argumento
                    navController.navigate("TelaAddCtt/$numeroCtt")
                }
            )
        }
        composable("TabScreen") {
            TabScreen(navController, authViewModel)
        }
        composable(
            route = "TelaAddCtt/{numeroCtt}",
            arguments = listOf(navArgument("numeroCtt") { type = NavType.StringType })
        ) { backStackEntry ->
            val numeroCtt = backStackEntry.arguments?.getString("numeroCtt") ?: ""

            AddCtt(
                numeroCtt = numeroCtt,
                contatoViewModel,
                navController
            )
        }
        composable("meuCodigo") { TelaQR(navController, authViewModel) }
        composable("escanearCodigo") { TelaEscanearCodigo() }
    }
}