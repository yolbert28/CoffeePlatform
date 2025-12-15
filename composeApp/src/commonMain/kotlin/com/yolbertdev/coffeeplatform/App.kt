package com.yolbertdev.coffeeplatform

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.yolbertdev.coffeeplatform.ui.home.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(screen = HomeScreen())
    }
}