package com.example.telasparcial.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await




class AuthViewModel : ViewModel(){

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> = _userState

    //feedback UI
    private val _authFeedback = MutableStateFlow<String?>(null)
    val authFeedback: StateFlow<String?> = _authFeedback

    //loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

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
            }catch (e: Exception){
                _authFeedback.value = e.message?: "Erro no Login :/"
            }finally {
                _loading.value = false
            }
        }
    }
    // NOVO: StateFlow para controlar a necessidade de reautenticação
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
                if (!senha.isNullOrEmpty()) {
                    user.updatePassword(senha).await()
                    _authFeedback.value = "email e senha atualizados com sucesso!"
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
                // Captura exceções do Firebase (ex: senha fraca, e-mail inválido, login recente necessário, etc.)
                _authFeedback.value   = "Sessão expirada. Por favor, insira sua senha novamente para confirmar a alteração."
                _requiresReAuth.value = true
            }catch (e: Exception){
              _authFeedback.value = e.message?: "Erro ao Atualizar Perfil"
            } finally {
                _loading.value = false
            }
        }
    }
    // NOVA FUNÇÃO: Para reautenticar o usuário
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
    }


    fun clearFeedBack(i: Int) {
        _authFeedback.value = null
    }

}