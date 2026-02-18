package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.data.database.dao.CustomerDao
import com.yolbertdev.coffeeplatform.data.database.mappers.CustomerMapper
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomerRepositoryImpl(
    private val dao: CustomerDao
) : CustomerRepository {

    override suspend fun insert(customer: Customer) {
        dao.insert(CustomerMapper.toDb(customer))
    }

    override suspend fun selectAll(): List<Customer> {
        return dao.selectAll().map { CustomerMapper.toDomain(it) }
    }

    override fun getCustomerById(id: Int): Flow<Customer?> {
        return dao.selectByIdFlow(id.toLong()).map { customerDb ->
            customerDb?.let { CustomerMapper.toDomain(it) }
        }
    }

    override suspend fun update(customer: Customer) {
        dao.update(CustomerMapper.toDb(customer))
    }
}