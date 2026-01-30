package com.yolbertdev.coffeeplatform.domain.model

data class User(
    val id: Long,
    val username: String,
    val name: String
    // No guardamos el password en el modelo de dominio por seguridad
)