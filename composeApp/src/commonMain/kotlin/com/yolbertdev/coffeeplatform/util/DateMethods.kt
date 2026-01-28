package com.yolbertdev.coffeeplatform.util

// En commonMain
expect object DateMethods {
    fun getCurrentTimeMillis(): Long

    // Agregamos esta función para convertir el Long a String legible
    fun formatDate(timestamp: Long): String
}