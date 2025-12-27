package com.yolbertdev.coffeeplatform.di

import app.cash.sqldelight.db.SqlDriver
import com.yolbertdev.coffeeplatform.data.local.DatabaseDriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single<SqlDriver> { DatabaseDriverFactory().createDriver() }
}