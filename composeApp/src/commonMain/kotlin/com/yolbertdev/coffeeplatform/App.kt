package com.yolbertdev.coffeeplatform

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.yolbertdev.coffeeplatform.ui.login.LoginScreen
import com.yolbertdev.coffeeplatform.ui.main.screens.LocalPdfOpener
import com.yolbertdev.coffeeplatform.ui.theme.CoffeePlatformTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(onOpenPdf: (String) -> Unit = {}) {
    CoffeePlatformTheme {
        CompositionLocalProvider(LocalPdfOpener provides onOpenPdf) {
            Navigator(screen = LoginScreen())
        }
    }
}