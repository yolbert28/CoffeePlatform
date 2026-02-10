package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository

class UpdateCustomerUseCase(private val repository: CustomerRepository) {
    suspend operator fun invoke(customer: Customer) {
        repository.update(customer)
    }
}