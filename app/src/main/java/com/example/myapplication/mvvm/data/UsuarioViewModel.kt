package com.example.myapplication.mvvm.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.database.entities.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- UiState: Define o estado completo da tela ---
data class UsuarioUiState(
    val usuarioPrincipal: Usuario? = null,
    val isLoading: Boolean = true,
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
    val snackbarAction: String = ""
) {
    val textoBotao: String
        get() = if (usuarioPrincipal == null) "Criar Conta" else "Salvar Alterações"
}

// --- ViewModel ---
class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.buscarUsuarioUnicoFlow().collect { usuario ->
                _uiState.update { currentState ->
                    // Ao carregar, preenche os campos com os dados do usuário, se existir
                    if (usuario != null) {
                        currentState.copy(
                            usuarioPrincipal = usuario,
                            nome = usuario.nome,
                            email = usuario.email,
                            senha = usuario.senha,
                            cpf = usuario.cpf,
                            telefone = usuario.telefone ?: "",
                            isLoading = false
                        )
                    } else {
                        // Limpa os campos se o usuário for deletado ou não existir
                        currentState.copy(
                            usuarioPrincipal = null,
                            nome = "",
                            email = "",
                            senha = "",
                            cpf = "",
                            telefone = "",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    // --- Eventos da UI (Eventos) ---
    fun onNomeChange(novoNome: String) = _uiState.update { it.copy(nome = novoNome) }
    fun onEmailChange(novoEmail: String) = _uiState.update { it.copy(email = novoEmail) }
    fun onSenhaChange(novaSenha: String) = _uiState.update { it.copy(senha = novaSenha) }
    fun onCpfChange(novoCpf: String) = _uiState.update { it.copy(cpf = novoCpf) }
    fun onTelefoneChange(novoTelefone: String) = _uiState.update { it.copy(telefone = novoTelefone) }
    fun onSnackbarDismiss() = _uiState.update { it.copy(showSnackbar = false) } // Evento para fechar o snackbar

    fun onSave() {
        val state = _uiState.value
        if (state.nome.isBlank() || state.email.isBlank() || state.senha.isBlank() || state.cpf.isBlank()) return

        val telefoneFinal = state.telefone.ifBlank { null }
        var mensagem: String
        var acao: String

        val usuarioParaSalvar = state.usuarioPrincipal?.copy(
            nome = state.nome,
            email = state.email,
            senha = state.senha,
            cpf = state.cpf,
            telefone = telefoneFinal
        ) ?: Usuario(
            nome = state.nome,
            email = state.email,
            senha = state.senha,
            cpf = state.cpf,
            telefone = telefoneFinal
        )

        viewModelScope.launch {
            if (state.usuarioPrincipal == null) {
                // 1. Ação no banco de dados
                repository.inserir(usuarioParaSalvar)
                mensagem = "Conta criada com sucesso!"
                acao = "CREATE"

                // 2. Atualiza o uiState imediatamente
                _uiState.update {
                    it.copy(
                        usuarioPrincipal = usuarioParaSalvar, // <--- ESTE É O PULP DO GATO!
                        showSnackbar = true,
                        snackbarMessage = mensagem,
                        snackbarAction = acao
                    )
                }

            } else {
                repository.atualizar(usuarioParaSalvar)
                mensagem = "Conta editada com sucesso!"
                acao = "EDIT"

                _uiState.update {
                    it.copy(
                        usuarioPrincipal = usuarioParaSalvar,
                        showSnackbar = true,
                        snackbarMessage = mensagem,
                        snackbarAction = acao
                    )
                }
            }
        }
    }

    fun onDelete() {
        val usuario = _uiState.value.usuarioPrincipal ?: return

        viewModelScope.launch {
            repository.deletar(usuario)

            // Exibir Snackbar após a deleção
            _uiState.update {
                it.copy(
                    showSnackbar = true,
                    snackbarMessage = "Conta excluída com sucesso!",
                    snackbarAction = "DELETE"
                )
            }
        }
    }
}

// --- Factory ---
class UsuarioViewModelFactory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}