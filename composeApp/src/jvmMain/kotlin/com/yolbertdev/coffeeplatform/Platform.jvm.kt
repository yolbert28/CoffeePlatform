package com.yolbertdev.coffeeplatform

import com.yolbertdev.coffeeplatform.data.local.DatabaseDriverFactory
import com.yolbertdev.coffeeplatform.util.ImageStorage
import org.koin.dsl.module

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

val platformModule = module {
    single { DatabaseDriverFactory() }

    // AQUÍ AGREGAMOS LA DEFINICIÓN PARA DESKTOP (sin argumentos)
    single { ImageStorage() }
}