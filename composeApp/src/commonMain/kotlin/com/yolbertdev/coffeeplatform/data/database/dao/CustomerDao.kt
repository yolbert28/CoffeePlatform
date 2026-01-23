package com.yolbertdev.coffeeplatform.data.database.dao

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
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
            photo = customer.photo, // Pasa null si la columna es nulable y no tienes foto
            status_id = customer.status_id
        )

        println("Customer inserted: ${queries.selectAll().executeAsList()}")

    }

    fun selectAll(): List<CustomerDb> {
        return queries.selectAll().executeAsList()
    }

}
