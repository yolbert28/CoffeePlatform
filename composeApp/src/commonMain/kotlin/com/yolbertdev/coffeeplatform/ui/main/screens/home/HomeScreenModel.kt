package com.yolbertdev.coffeeplatform.ui.main.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import com.yolbertdev.coffeeplatform.data.local.LocalDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenModel(
    private val localDatabase: LocalDatabase
) : ScreenModel {
    private val _state = MutableStateFlow("name")
    val state = _state.asStateFlow()

    init {
        println(localDatabase.createDatabase())
    }

    fun startt(){
        println(localDatabase.createDatabase())
    }

}
