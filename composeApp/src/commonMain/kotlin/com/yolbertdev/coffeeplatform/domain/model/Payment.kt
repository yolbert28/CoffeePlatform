package com.yolbertdev.coffeeplatform.domain.model

data class Payment(
    val id: Long,
    val amount: Double,
    val paymentType: String, // String ("USD" o "QT")
    val customerId: Long,
    val creationDate: Long,
    val updateDate: Long
)