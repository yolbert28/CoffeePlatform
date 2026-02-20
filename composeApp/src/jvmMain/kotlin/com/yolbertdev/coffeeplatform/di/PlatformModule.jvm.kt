package com.yolbertdev.coffeeplatform.di

import app.cash.sqldelight.db.SqlDriver
import com.yolbertdev.coffeeplatform.data.local.DatabaseDriverFactory
import com.yolbertdev.coffeeplatform.domain.ports.BackupFileManager
import com.yolbertdev.coffeeplatform.domain.ports.PdfGenerator
import com.yolbertdev.coffeeplatform.pdf.JvmPdfGenerator
import com.yolbertdev.coffeeplatform.sync.JvmBackupFileManager
import com.yolbertdev.coffeeplatform.util.ImageStorage
import org.koin.core.module.Module // <-- 1. Importante: Importar Module
import org.koin.dsl.module

// 2. Importante: Especificar el tipo explícito ": Module"
actual val platformModule: Module = module {

    // Ahora Koin utiliza nuestra Factory segura multiplataforma
    single<SqlDriver> {
        DatabaseDriverFactory().createDriver()
    }

    single { ImageStorage() }

    // Definimos la implementación para Desktop
    single<PdfGenerator> { JvmPdfGenerator() }
    single<BackupFileManager> { JvmBackupFileManager() }
}