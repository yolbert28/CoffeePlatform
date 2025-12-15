package com.yolbertdev.coffeeplatform.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    Text("hello")
                }
            )
        }
    ){ innerPadding ->
        Text(uiState.value.name)
    }
}
