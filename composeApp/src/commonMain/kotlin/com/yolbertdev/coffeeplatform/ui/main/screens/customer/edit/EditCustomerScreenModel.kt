package com.yolbertdev.coffeeplatform.ui.main.screens.customer.edit

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.usecase.UpdateCustomerUseCase
import com.yolbertdev.coffeeplatform.util.ImageStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditCustomerState(
    val id: Long = 0,
    val idCard: String = "",
    val name: String = "",
    val nickname: String = "",
    val description: String = "",
    val location: String = "",
    val photo: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

class EditCustomerScreenModel(
    private val updateCustomerUseCase: UpdateCustomerUseCase,
    private val imageStorage: ImageStorage // Inyectamos la clase
) : ScreenModel {

    private val _state = MutableStateFlow(EditCustomerState())
    val state = _state.asStateFlow()
    private var originalCustomer: Customer? = null

    fun init(customer: Customer) {
        if (originalCustomer != null) return // Evitar reiniciar si ya se cargó
        originalCustomer = customer
        _state.update {
            it.copy(
                id = customer.id,
                idCard = customer.idCard ?: "",
                name = customer.name,
                nickname = customer.nickname,
                description = customer.description,
                location = customer.location,
                photo = customer.photo?:""
            )
        }
    }

    fun onIdCardChange(v: String) = _state.update { it.copy(idCard = v) }
    fun onNameChange(v: String) = _state.update { it.copy(name = v) }
    fun onNicknameChange(v: String) = _state.update { it.copy(nickname = v) }
    fun onDescriptionChange(v: String) = _state.update { it.copy(description = v) }
    fun onLocationChange(v: String) = _state.update { it.copy(location = v) }

    fun onPhotoSelected(bytes: ByteArray) {
        screenModelScope.launch {
            val path = imageStorage.saveImage(bytes)
            if (path != null) {
                _state.update { it.copy(photo = path) }
            }
        }
    }

    fun save() {
        val current = _state.value
        val original = originalCustomer ?: return

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val updated = original.copy(
                    idCard = current.idCard,
                    name = current.name,
                    nickname = current.nickname,
                    description = current.description,
                    location = current.location,
                    photo = current.photo
                )
                updateCustomerUseCase(updated)
                _state.update { it.copy(isSaved = true, isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}