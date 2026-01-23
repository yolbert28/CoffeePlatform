package com.yolbertdev.coffeeplatform.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import java.util.Properties

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(
            url = "jdbc:sqlite:CoffeeDatabase.db",
            Properties().apply { put("foreign_keys", "true") },
            CoffeeDatabase.Companion.Schema
        )
    }
}