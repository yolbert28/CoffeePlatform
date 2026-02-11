package com.yolbertdev.coffeeplatform.domain.model

data class Payment(
    val id: Long,
    val loanId: Int,
    val amount: Double,
    val paymentType: String, // String ("USD" o "QT")
    val customerId: Int,
    val creationDate: Long,
    val updateDate: Long,
    val note: String?
)