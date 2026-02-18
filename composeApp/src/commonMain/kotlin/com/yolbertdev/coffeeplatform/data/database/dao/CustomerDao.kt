package com.yolbertdev.coffeeplatform.data.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.util.DateMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import com.yolbertdev.coffeeplatform.db.Customer as CustomerDb

class CustomerDao(
    private val database: CoffeeDatabase
){
    private val queries = database.customerQueries

    fun insert(customer: CustomerDb) {
        queries.insert(
            id_card = customer.id_card,
            name = customer.name,
            nickname = customer.nickname,
            description = customer.description,
            credit_level = customer.credit_level,
            location = customer.location,
            photo = customer.photo,
            status_id = customer.status_id
        )
    }

    fun selectAll(): List<CustomerDb> {
        return queries.selectAll().executeAsList()
    }

    fun selectByIdFlow(id: Long): Flow<CustomerDb?> {
        return queries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }

    fun update(customer: CustomerDb) {
        database.customerQueries.update(
            name = customer.name,
            nickname = customer.nickname,
            description = customer.description,
            location = customer.location,
            photo = customer.photo,
            updateDate = DateMethods.getCurrentTimeMillis(),
            id = customer.id
        )
    }
}