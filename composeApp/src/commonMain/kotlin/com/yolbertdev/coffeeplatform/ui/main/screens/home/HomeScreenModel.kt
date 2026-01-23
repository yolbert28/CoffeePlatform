package com.yolbertdev.coffeeplatform.ui.main.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenModel(
) : ScreenModel {
    private val _state = MutableStateFlow("name")
    val state = _state.asStateFlow()

}
