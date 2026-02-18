package com.yolbertdev.coffeeplatform.domain.repository

import com.yolbertdev.coffeeplatform.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    suspend fun insert(customer: Customer)

    suspend fun selectAll(): List<Customer>

    fun getCustomerById(id: Int): Flow<Customer?>

    suspend fun update(customer: Customer)
}