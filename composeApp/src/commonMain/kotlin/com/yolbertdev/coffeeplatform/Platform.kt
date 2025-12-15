package com.yolbertdev.coffeeplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform