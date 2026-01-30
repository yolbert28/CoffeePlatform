package com.yolbertdev.coffeeplatform.ui.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.yolbertdev.coffeeplatform.domain.model.UserSession
data class LoginState(
    val username: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)

class LoginScreenModel(
    private val loginUseCase: LoginUseCase
) : ScreenModel {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onUsernameChange(v: String) = _state.update { it.copy(username = v, error = null) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v, error = null) }

    fun login() {
        val s = _state.value
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val user = loginUseCase(s.username, s.password)
            if (user != null) {
                UserSession.currentUser = user
                _state.update { it.copy(isLoggedIn = true, isLoading = false) }
            } else {
                _state.update { it.copy(isLoading = false, error = "Credenciales incorrectas") }
            }
        }
    }
}