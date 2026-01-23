package com.yolbertdev.coffeeplatform.domain.repository

import com.yolbertdev.coffeeplatform.domain.model.Customer

interface CustomerRepository {

    suspend fun insert(customer: Customer)

    suspend fun selectAll(): List<Customer>

}
