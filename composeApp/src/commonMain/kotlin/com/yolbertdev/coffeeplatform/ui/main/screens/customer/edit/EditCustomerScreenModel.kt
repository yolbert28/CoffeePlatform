package com.yolbertdev.coffeeplatform.ui.main.screens.customer.edit
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.usecase.UpdateCustomerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditCustomerUiState(
    val id: Long = 0,
    val name: String = "",
    val nickname: String = "",
    val description: String = "",
    val location: String = "",
    val photo: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

class EditCustomerScreenModel(
    private val updateCustomerUseCase: UpdateCustomerUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(EditCustomerUiState())
    val state = _state.asStateFlow()

    // Objeto original para mantener datos que no se editan (como idCard o creationDate)
    private var originalCustomer: Customer? = null

    // Inicializamos con los datos del cliente que recibimos de la pantalla anterior
    fun initCustomer(customer: Customer) {
        originalCustomer = customer
        _state.update {
            it.copy(
                id = customer.id,
                name = customer.name,
                nickname = customer.nickname,
                description = customer.description,
                location = customer.location,
                photo = customer.photo
            )
        }
    }

    fun onNameChange(v: String) = _state.update { it.copy(name = v) }
    fun onNicknameChange(v: String) = _state.update { it.copy(nickname = v) }
    fun onDescriptionChange(v: String) = _state.update { it.copy(description = v) }
    fun onLocationChange(v: String) = _state.update { it.copy(location = v) }
    fun onPhotoChange(v: String) = _state.update { it.copy(photo = v) }

    fun saveChanges() {
        val s = _state.value
        val original = originalCustomer ?: return

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val updatedCustomer = original.copy(
                    name = s.name,
                    nickname = s.nickname,
                    description = s.description,
                    location = s.location,
                    photo = s.photo
                    // updateDate se maneja en el DAO o Repo
                )

                updateCustomerUseCase(updatedCustomer)
                _state.update { it.copy(isSaved = true, isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}