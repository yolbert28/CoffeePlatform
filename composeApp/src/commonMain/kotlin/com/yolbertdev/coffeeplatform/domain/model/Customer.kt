package com.yolbertdev.coffeeplatform.domain.model

data class Customer(
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