package com.yolbertdev.coffeeplatform

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.yolbertdev.coffeeplatform.ui.login.LoginScreen
import com.yolbertdev.coffeeplatform.ui.main.screens.LocalPdfOpener
import com.yolbertdev.coffeeplatform.ui.theme.CoffeePlatformTheme
// 1. Imports necesarios para la base de datos
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(onOpenPdf: (String) -> Unit = {}) {
    CoffeePlatformTheme {
        // 2. Inyectamos la base de datos
        val db = koinInject<CoffeeDatabase>()

        // 3. Inicializamos datos maestros (Monedas y Estados) al arrancar
        LaunchedEffect(Unit) {
            initDatabaseData(db)
        }

        CompositionLocalProvider(LocalPdfOpener provides onOpenPdf) {
            Navigator(screen = LoginScreen())
        }
    }
}

// 4. Lógica para llenar las tablas si están vacías
private fun initDatabaseData(db: CoffeeDatabase) {
    try {
        val paymentQueries = db.paymentTypeQueries
        val statusQueries = db.loanStatusQueries

        // -- Tipos de Pago --
        val paymentTypes = paymentQueries.selectAll().executeAsList()
        if (paymentTypes.isEmpty()) {
            // CAMBIO: Usamos insertNew que ahora pedirá (name: String)
            paymentQueries.insertNew("USD")
            paymentQueries.insertNew("QT")
            println("DB Init: Tipos de pago creados.")
        }

        // -- Estados de Préstamo --
        val loanStatuses = statusQueries.selectAll().executeAsList()
        if (loanStatuses.isEmpty()) {
            // CAMBIO: Usamos insertNew
            statusQueries.insertNew("Pendiente")
            statusQueries.insertNew("Pagado")
            statusQueries.insertNew("Vencido")
            println("DB Init: Estados de préstamo creados.")
        }

    } catch (e: Exception) {
        println("Error inicializando BD: ${e.message}")
        e.printStackTrace()
    }
}