package com.yolbertdev.coffeeplatform.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import java.io.File
import java.util.Properties

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val userHome = System.getProperty("user.home")
        val appFolder = File(userHome, ".coffeeplatform")

        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }

        val dbFile = File(appFolder, "CoffeeDatabase.db")

        // Formateo seguro: Reemplazamos \ por / para evitar errores de escape en la ruta JDBC
        val safeDbPath = dbFile.absolutePath.replace("\\", "/")

        return JdbcSqliteDriver(
            url = "jdbc:sqlite:$safeDbPath",
            Properties().apply { put("foreign_keys", "true") },
            CoffeeDatabase.Companion.Schema
        )
    }
}