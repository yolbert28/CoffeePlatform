package com.yolbertdev.coffeeplatform.domain.model

data class Loan(
    val id: Int,
    val customerId: Int,
    val interestRate: Double, // CORREGIDO: De Long a Double (coincide con REAL)
    val description: String,
    val paymentDate: Long,
    val paymentType: String,
    val quantity: Double,
    val paid: Double,         // CORREGIDO: De String a Double (coincide con REAL)
    val statusId: Int,
    val creationDate: Long,
    val updateDate: Long
)