package com.yolbertdev.coffeeplatform.ui.main.screens.customer

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.usecase.InsertCustomerUseCase
import com.yolbertdev.coffeeplatform.domain.usecase.SelectAllCustomerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomerScreenModel(
    private val insertCustomerUseCase: InsertCustomerUseCase,
    private val selectAllCustomerUseCase: SelectAllCustomerUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState = _uiState.asStateFlow()


    fun insertCustomer(customer: Customer) {
        screenModelScope.launch {
            insertCustomerUseCase(customer)
        }
    }

    fun getAllCustomers() {
        screenModelScope.launch {
            val customers = selectAllCustomerUseCase()
            _uiState.update {
                it.copy(
                    customers = customers
                )
            }
        }
    }
}

data class CustomerUiState(
    val customers: List<Customer> = emptyList(),
)