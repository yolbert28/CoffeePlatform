package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository

class InsertCustomerUseCase(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(customer: Customer){
        return customerRepository.insert(customer)
    }
}