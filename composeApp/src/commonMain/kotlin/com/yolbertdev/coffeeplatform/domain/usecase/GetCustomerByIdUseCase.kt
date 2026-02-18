package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow

class GetCustomerByIdUseCase(
    private val repository: CustomerRepository
) {
    operator fun invoke(id: Int): Flow<Customer?> {
        return repository.getCustomerById(id)
    }
}