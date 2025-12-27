package com.yolbertdev.coffeeplatform.di

import com.yolbertdev.coffeeplatform.data.local.LocalDatabase
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.ui.main.screens.home.HomeScreenModel
import org.koin.dsl.module

val commonModule = module {
    single<CoffeeDatabase> { CoffeeDatabase(get()) }
    single<LocalDatabase> { LocalDatabase(get()) }
    single<HomeScreenModel> { HomeScreenModel(get()) }
}