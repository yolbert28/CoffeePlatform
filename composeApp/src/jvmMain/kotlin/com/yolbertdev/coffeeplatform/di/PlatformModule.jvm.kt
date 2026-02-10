package com.yolbertdev.coffeeplatform.di

import app.cash.sqldelight.db.SqlDriver
import com.yolbertdev.coffeeplatform.data.local.DatabaseDriverFactory
import com.yolbertdev.coffeeplatform.util.ImageStorage
import org.koin.dsl.module

actual val platformModule = module {
    single<SqlDriver> { DatabaseDriverFactory().createDriver() }
    single { ImageStorage() }
}