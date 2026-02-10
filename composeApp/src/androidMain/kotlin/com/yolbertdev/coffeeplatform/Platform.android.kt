package com.yolbertdev.coffeeplatform

import android.os.Build
import com.yolbertdev.coffeeplatform.data.local.DatabaseDriverFactory
import com.yolbertdev.coffeeplatform.util.ImageStorage
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
val platformModule = module {
    single { DatabaseDriverFactory(get()) }

    // AQUÍ AGREGAMOS LA DEFINICIÓN PARA ANDROID (que usa Context)
    single { ImageStorage(get()) }
}