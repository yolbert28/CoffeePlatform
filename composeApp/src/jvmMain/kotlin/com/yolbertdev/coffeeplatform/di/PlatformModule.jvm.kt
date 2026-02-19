package com.yolbertdev.coffeeplatform.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.yolbertdev.coffeeplatform.data.local.DatabaseDriverFactory
import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.ports.BackupFileManager
import com.yolbertdev.coffeeplatform.domain.ports.PdfGenerator
import com.yolbertdev.coffeeplatform.pdf.JvmPdfGenerator
import com.yolbertdev.coffeeplatform.sync.JvmBackupFileManager
import com.yolbertdev.coffeeplatform.util.ImageStorage
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {
    single<SqlDriver> {
        val driver: SqlDriver = JdbcSqliteDriver(
            url = "jdbc:sqlite:${getDatabasePath()}",
        )
        CoffeeDatabase.Schema.create(driver)
        driver
    }
    single { ImageStorage() }
    // Definimos la implementación para Desktop
    single<PdfGenerator> { JvmPdfGenerator() }
    single<BackupFileManager> { JvmBackupFileManager() }
}
private fun getDatabasePath(): String {
    val dbFile = File(System.getProperty("user.home"), "coffee_platform.db")
    return dbFile.absolutePath
}