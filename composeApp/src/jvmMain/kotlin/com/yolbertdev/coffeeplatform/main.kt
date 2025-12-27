package com.yolbertdev.coffeeplatform

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.yolbertdev.coffeeplatform.di.appModule
import org.koin.core.context.startKoin

fun main() = application {

    startKoin {
        modules(appModule())
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 1280.dp, height = 720.dp),
        title = "CoffeePlatform",
    ) {
        App()
    }
}