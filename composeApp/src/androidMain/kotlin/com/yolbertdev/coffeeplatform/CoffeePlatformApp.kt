package com.yolbertdev.coffeeplatform

import android.app.Application
import com.yolbertdev.coffeeplatform.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CoffeePlatformApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CoffeePlatformApp)
            modules(appModule())
        }
    }
}