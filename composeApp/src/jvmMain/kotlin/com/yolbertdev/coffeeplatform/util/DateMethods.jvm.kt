package com.yolbertdev.coffeeplatform.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object DateMethods {
    actual fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    actual fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}