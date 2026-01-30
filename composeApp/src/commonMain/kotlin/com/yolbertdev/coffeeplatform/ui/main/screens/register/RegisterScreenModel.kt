package com.yolbertdev.coffeeplatform.ui.main.screens.register

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterState(
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false
)

class RegisterScreenModel(
    private val registerUseCase: RegisterUseCase
) : ScreenModel {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onNameChange(v: String) = _state.update { it.copy(name = v, error = null) }
    fun onUsernameChange(v: String) = _state.update { it.copy(username = v, error = null) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v, error = null) }
    fun onConfirmChange(v: String) = _state.update { it.copy(confirmPassword = v, error = null) }

    fun register() {
        val s = _state.value
        if (s.name.isBlank() || s.username.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Todos los campos son obligatorios") }
            return
        }
        if (s.password != s.confirmPassword) {
            _state.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val success = registerUseCase(s.username, s.password, s.name)
            if (success) {
                _state.update { it.copy(isSuccess = true, isLoading = false) }
            } else {
                _state.update { it.copy(isLoading = false, error = "El usuario ya existe") }
            }
        }
    }
}