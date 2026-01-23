package com.yolbertdev.coffeeplatform.data.local

import app.cash.sqldelight.db.SqlDriver
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}



