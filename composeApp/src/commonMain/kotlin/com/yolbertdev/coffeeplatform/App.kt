package com.yolbertdev.coffeeplatform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.yolbertdev.coffeeplatform.ui.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.yolbertdev.coffeeplatform.ui.login.LoginScreen
import com.yolbertdev.coffeeplatform.ui.theme.CoffeePlatformTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    CoffeePlatformTheme {
        Navigator(screen = LoginScreen())
    }
}