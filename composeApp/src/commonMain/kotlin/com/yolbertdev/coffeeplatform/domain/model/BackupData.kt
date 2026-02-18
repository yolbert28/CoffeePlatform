package com.yolbertdev.coffeeplatform.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val version: Int = 1,
    val exportDate: Long,
    val appName: String = "CoffeePlatform",
    val customers: List<CustomerBackup>,
    val loans: List<LoanBackup>,
    val payments: List<PaymentBackup>,
    val images: Map<String, String> = emptyMap() // Clave: NombreArchivo, Valor: Base64
)

@Serializable
data class CustomerBackup(
    val id: Long,
    val idCard: String?,
    val name: String,
    val nickname: String,
    val description: String,
    val creditLevel: Long,
    val location: String,
    val photo: String?,
    val creationDate: Long,
    val updateDate: Long,
    val statusId: Long
)

@Serializable
data class LoanBackup(
    val id: Int,
    val customerId: Int,
    val interestRate: Double,
    val paymentTypeId: Long,
    val amount: Double,
    val paymentDate: Long,
    val paid: Double,
    val description: String,
    val creationDate: Long,
    val updateDate: Long,
    val statusId: Int
)

@Serializable
data class PaymentBackup(
    val id: Long,
    val loanId: Long,
    val amount: Double,
    val paymentDate: Long,
    val note: String?,
    val creationDate: Long,
    val updateDate: Long
)