package com.yolbertdev.coffeeplatform.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        Text(text = "Home Screen")
    }
}