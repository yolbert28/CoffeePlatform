package com.yolbertdev.coffeeplatform.ui.main.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.model.UserSession
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val userName: String = "Usuario",
    val totalQuintales: Double = 0.0, // Total en QT
    val totalDollars: Double = 0.0,   // Total en USD
    val recentLoans: List<Pair<Loan, Customer>> = emptyList(), // Lista real
    val isLoading: Boolean = false
)

class HomeScreenModel(
    private val loanRepository: LoanRepository
) : ScreenModel {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    fun loadData() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentUser = UserSession.currentUser
            val name = currentUser?.name ?: "Invitado"

            try {
                // Obtenemos todos los préstamos
                val loans = loanRepository.getAllLoansWithCustomer()

                // Calculamos totales separados
                val quintales = loans
                    .filter { it.first.paymentType == "QT" }
                    .sumOf { it.first.quantity }

                val dollars = loans
                    .filter { it.first.paymentType == "USD" }
                    .sumOf { it.first.quantity }

                // Obtenemos los 5 más recientes (asumiendo que el ID más alto es el más reciente o podrías ordenar por fecha)
                val recent = loans.sortedByDescending { it.first.creationDate }.take(5)

                _state.update {
                    it.copy(
                        userName = name,
                        totalQuintales = quintales,
                        totalDollars = dollars,
                        recentLoans = recent,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(userName = name, isLoading = false) }
            }
        }
    }
}