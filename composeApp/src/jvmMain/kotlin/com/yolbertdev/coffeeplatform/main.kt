package com.yolbertdev.coffeeplatform

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 1280.dp, height = 720.dp),
        title = "CoffeePlatform",
    ) {
        App()
    }
}