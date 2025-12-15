package com.yolbertdev.coffeeplatform.view

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState(name = "World"))
    val uiState: StateFlow<MainUiState> = _uiState

}

data class MainUiState(
    val name: String
)