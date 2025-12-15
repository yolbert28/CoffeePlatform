package com.yolbertdev.coffeeplatform

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.yolbertdev.coffeeplatform.ui.login.LoginScreen
import com.yolbertdev.coffeeplatform.ui.theme.CoffeePlatformTheme

@Composable
@Preview
fun App() {
    CoffeePlatformTheme {
        Navigator(screen = LoginScreen())
    }
}