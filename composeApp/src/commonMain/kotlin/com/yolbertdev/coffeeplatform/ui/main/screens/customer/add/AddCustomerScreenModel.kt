package com.yolbertdev.coffeeplatform.ui.main.screens.customer.add

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.Bitmap
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.usecase.InsertCustomerUseCase
import com.yolbertdev.coffeeplatform.util.ImageStorage
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCustomerScreenModel(
    private val insertCustomerUseCase: InsertCustomerUseCase,
    private val imageStorage: ImageStorage
) : ScreenModel {

    private val _uiState = MutableStateFlow(AddCustomerUiState())
    val uiState = _uiState.asStateFlow()

    fun onPhotoCaptured(bytes: ByteArray) {
        _uiState.update { it.copy(photoBytes = bytes) }
    }

    fun onChangeName(name: String) {
        _uiState.update {
            it.copy(
                name = name
            )
        }
    }

    fun onChangeNickname(nickname: String) {
        _uiState.update {
            it.copy(
                nickname = nickname
            )
        }
    }

    fun onChangeIdCard(idCard: String) {
        _uiState.update {
            it.copy(
                idCard = idCard
            )
        }
    }

    fun onChangeLocation(location: String) {
        _uiState.update {
            it.copy(
                location = location
            )
        }
    }

    fun onChangeDescription(description: String) {
        _uiState.update {
            it.copy(
                description = description
            )
        }
    }


    fun insertCustomer(onSuccess: () -> Unit) {
        val currentState = _uiState.value

        screenModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // 1. Si hay un bitmap capturado, lo guardamos físicamente primero
                    val photoPath = currentState.photoBytes?.let { bytes ->
                        imageStorage.saveImage(bytes)
                    } ?: ""

                    // Debug para verificar en consola (usa println para ver el salto de línea)
                    println("DEBUG: Photo saved at -> $photoPath")

                    // 2. Creamos el objeto Customer con la RUTA de la foto
                    val customer = Customer(
                        id = 0L,
                        idCard = currentState.idCard,
                        name = currentState.name,
                        nickname = currentState.nickname,
                        description = currentState.description,
                        creditLevel = 1L,
                        location = currentState.location,
                        photo = photoPath, // Guardamos el path
                        creationDate = DateMethods.getCurrentTimeMillis(),
                        updateDate = DateMethods.getCurrentTimeMillis(),
                        statusId = 1L
                    )

                    insertCustomerUseCase(customer)
                    onSuccess()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}


data class AddCustomerUiState(
    val photoBytes: ByteArray? = null,
    val name: String = "",
    val nickname: String = "",
    val idCard: String = "",
    val location: String = "",
    val description: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddCustomerUiState

        if (!photoBytes.contentEquals(other.photoBytes)) return false
        if (name != other.name) return false
        if (nickname != other.nickname) return false
        if (idCard != other.idCard) return false
        if (location != other.location) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = photoBytes?.contentHashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + idCard.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}
