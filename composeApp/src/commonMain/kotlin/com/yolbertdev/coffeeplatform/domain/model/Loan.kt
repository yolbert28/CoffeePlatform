package com.yolbertdev.coffeeplatform.domain.model

data class Loan(
    val id: Int,
    val customerId: Int,
    val interestRate: Double,
    val description: String,
    val paymentDate: String,
    val paymentType: String,
    val quantity: Double,
    val paid: Double,
    val statusId: Int,
    val creationDate: String,
    val updateDate: String
)