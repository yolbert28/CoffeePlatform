package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository

class SelectAllCustomerUseCase(
    private val customerRepository: CustomerRepository
) {
    suspend operator fun invoke(): List<Customer> {
        return customerRepository.selectAll()
    }
}