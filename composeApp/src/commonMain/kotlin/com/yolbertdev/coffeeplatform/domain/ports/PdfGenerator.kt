package com.yolbertdev.coffeeplatform.domain.ports

import com.yolbertdev.coffeeplatform.domain.model.ClientDebt

interface PdfGenerator {
    // Retornamos la ruta del archivo generado o un ByteArray si prefieres manejarlo en memoria.
    // Para reportes, guardar en disco temporal suele ser mejor.
    suspend fun generateDebtReport(clients: List<ClientDebt>): String
}