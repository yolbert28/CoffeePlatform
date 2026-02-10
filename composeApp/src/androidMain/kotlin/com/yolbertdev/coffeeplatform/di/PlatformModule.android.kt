package com.yolbertdev.coffeeplatform.di

import app.cash.sqldelight.db.SqlDriver
import com.yolbertdev.coffeeplatform.data.local.DatabaseDriverFactory
import com.yolbertdev.coffeeplatform.domain.ports.PdfGenerator
import com.yolbertdev.coffeeplatform.pdf.AndroidPdfGenerator
import com.yolbertdev.coffeeplatform.util.ImageStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<SqlDriver> { DatabaseDriverFactory(get()).createDriver() }
    single<PdfGenerator> { AndroidPdfGenerator(context = androidContext()) }
    single { ImageStorage(get()) }
}