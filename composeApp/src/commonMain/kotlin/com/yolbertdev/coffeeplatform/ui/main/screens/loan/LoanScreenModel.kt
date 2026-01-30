package com.yolbertdev.coffeeplatform.ui.main.screens.loan

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.model.Loan
import com.yolbertdev.coffeeplatform.domain.repository.LoanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoanTabUiState(
    val isLoading: Boolean = false,
    val loans: List<Pair<Loan, Customer>> = emptyList(), // Lista completa
    val filteredLoans: List<Pair<Loan, Customer>> = emptyList(), // Lista filtrada
    val searchQuery: String = ""
)

class LoanScreenModel(
    private val repository: LoanRepository
) : ScreenModel {

    private val _uiState = MutableStateFlow(LoanTabUiState())
    val uiState = _uiState.asStateFlow()

    fun loadLoans() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = repository.getAllLoansWithCustomer()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loans = result,
                        filteredLoans = result // Inicialmente son iguales
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val filtered = if (query.isBlank()) {
                state.loans
            } else {
                state.loans.filter { (loan, customer) ->
                    customer.name.contains(query, ignoreCase = true) ||
                            loan.description.contains(query, ignoreCase = true)
                }
            }
            state.copy(searchQuery = query, filteredLoans = filtered)
        }
    }
}