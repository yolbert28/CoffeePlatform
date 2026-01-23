package com.yolbertdev.coffeeplatform.data.database.mappers

import com.yolbertdev.coffeeplatform.domain.model.Customer
import com.yolbertdev.coffeeplatform.db.Customer as CustomerDb

object CustomerMapper {
    fun toDomain(customer: CustomerDb): Customer {
        return Customer(
            id = customer.id,
            idCard = customer.id_card,
            name = customer.name,
            nickname = customer.nickname,
            description = customer.description,
            creditLevel = customer.credit_level,
            location = customer.location,
            photo = customer.photo,
            creationDate = customer.creation_date,
            updateDate = customer.update_date,
            statusId = customer.status_id
        )
    }

    fun toDb(customer: Customer): CustomerDb {
        return CustomerDb(
            id = customer.id,
            id_card = customer.idCard,
            name = customer.name,
            nickname = customer.nickname,
            description = customer.description,
            credit_level = customer.creditLevel,
            location = customer.location,
            photo = customer.photo,
            creation_date = customer.creationDate,
            update_date = customer.updateDate,
            status_id = customer.statusId
        )
    }
}