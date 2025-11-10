package com.example.telasparcial.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telasparcial.data.repository.AdminRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AuthViewModel : ViewModel(){
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _userState = MutableStateFlow(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> = _userState
    private val _authFeedback = MutableStateFlow<String?>(null)
    val authFeedback: StateFlow<String?> = _authFeedback
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Para verificar se o usuário é admin
    private lateinit var adminRepository: AdminRepository
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    init {
        if (auth.currentUser != null) {
            adminRepository = AdminRepository(fireStore = FirebaseFirestore.getInstance(), auth = auth)
            viewModelScope.launch {
                _isAdmin.value = adminRepository.isAdmin()
            }
        }
    }

    fun cadastrar(email: String, senha: String, nome: String){
        viewModelScope.launch {
            _loading.value = true
            _authFeedback.value = null

            try {

                // Tenta criar no firebase
                auth.createUserWithEmailAndPassword(email, senha).await()

                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build()

                auth.currentUser?.updateProfile(profileUpdate)?.await()

                _userState.value = auth.currentUser
                _authFeedback.value = "Cadastro realizado com sucesso! :) "

            }catch (e: Exception){
                _authFeedback.value = e.message ?: "Erro no cadastro :/ "
            }finally {
                _loading.value = false
            }
        }
    }

    fun login(email: String, senha: String){
        viewModelScope.launch {
            _loading.value = true
            _authFeedback.value = null
            try {
                auth.signInWithEmailAndPassword(email, senha).await()
                _userState.value = auth.currentUser
                _isAdmin.value = adminRepository.isAdmin()
            }catch (e: Exception){
                _authFeedback.value = e.message?: "Erro no Login :/"
            }finally {
                _loading.value = false
            }
        }
    }
    private val _requiresReAuth = MutableStateFlow(false)
    val requiresReAuth: StateFlow<Boolean> = _requiresReAuth
    // Função para resetar o estado de reautenticação
    fun reAuthHandled() {
        _requiresReAuth.value = false
    }

    fun atualizaruser(email: String? = null, senha: String? = null){
        viewModelScope.launch {
            _loading.value = true
            _authFeedback.value = null

            try {
                val user = auth.currentUser
                if (user == null) {
                    _authFeedback.value = "Nenhum usuário logado para atualizar."
                    _loading.value = false
                    return@launch
                }

                var perfilAtualizado = false

                // 1. Atualizar o e-mail
                if (!email.isNullOrEmpty() && user.email != email) {
                    user.updateEmail(email).await()
                    _authFeedback.value = "e-mail atualizado!"
                    perfilAtualizado = true
                }

                // 2. Atualizar a senha
                if (!senha.isNullOrBlank()) { // ✅ MELHORIA DE SEGURANÇA: Usando isNullOrBlank
                    user.updatePassword(senha).await()
                    _authFeedback.value = "Senha atualizada com sucesso!"
                    perfilAtualizado = true
                }

                // Atualiza o estado do usuário para refletir as mudanças na UI
                _userState.value = user

                if (perfilAtualizado) {
                    _authFeedback.value = "Perfil atualizado com sucesso!"
                } else {
                    _authFeedback.value = "Nenhuma alteração foi feita."
                }

            } catch (e: FirebaseAuthRecentLoginRequiredException) {
                _authFeedback.value   = "Sessão expirada. Por favor, insira sua senha novamente para confirmar a alteração."
                _requiresReAuth.value = true
            }catch (e: Exception){
                _authFeedback.value = e.message?: "Erro ao Atualizar Perfil"
            } finally {
                _loading.value = false
            }
        }
    }

    fun reauthenticateAndRetryUpdate(password: String, newEmail: String?, newPassword: String?) {
        viewModelScope.launch {
            _loading.value = true
            _authFeedback.value = null
            try {
                val user = auth.currentUser
                if (user?.email == null) {
                    _authFeedback.value = "Não foi possível reautenticar."
                    _loading.value = false
                    return@launch
                }

                // 1. Cria a credencial com o e-mail e a senha fornecida
                val credential = EmailAuthProvider.getCredential(user.email!!, password)

                // 2. Reautentica o usuário
                user.reauthenticate(credential).await()

                // 3. SE SUCESSO, TENTA A ATUALIZAÇÃO NOVAMENTE
                _authFeedback.value = "Autenticação confirmada! Tentando atualizar novamente..."
                atualizaruser(newEmail, newPassword)

            } catch (e: Exception) {
                _authFeedback.value = "Senha incorreta. Tente novamente."
            } finally {
                _loading.value = false
            }
        }
    }


    fun desLogar(){
        auth.signOut()
        _userState.value = null
        _isAdmin.value = false
    }


    fun clearFeedBack() {
        _authFeedback.value = null
    }
}