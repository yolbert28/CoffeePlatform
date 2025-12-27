package com.yolbertdev.coffeeplatform.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseDriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            CoffeeDatabase.Schema,
            context,
            "CoffeeDatabase.db"
        )
    }
}
