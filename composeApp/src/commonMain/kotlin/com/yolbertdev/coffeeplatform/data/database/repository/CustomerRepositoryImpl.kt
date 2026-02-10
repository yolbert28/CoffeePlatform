package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.data.database.dao.CustomerDao
import com.yolbertdev.coffeeplatform.data.database.mappers.CustomerMapper
import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.domain.repository.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.yolbertdev.coffeeplatform.db.Customer as CustomerDb

class CustomerRepositoryImpl(
    private val customerDao: CustomerDao
) : CustomerRepository {

    override suspend fun insert(customer: Customer) = withContext(Dispatchers.IO) {
        val customerDb: CustomerDb = CustomerMapper.toDb(customer)
        customerDao.insert(customerDb)
    }

    override suspend fun selectAll(): List<Customer> {
        return customerDao.selectAll().map {
            CustomerMapper.toDomain(it)
        }
    }
    override suspend fun update(customer: Customer) = withContext(Dispatchers.IO) {
        val customerDb: CustomerDb = CustomerMapper.toDb(customer)
        customerDao.update(customerDb)
    }
}