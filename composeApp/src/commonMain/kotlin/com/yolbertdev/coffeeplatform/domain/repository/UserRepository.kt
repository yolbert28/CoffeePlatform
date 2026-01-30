package com.yolbertdev.coffeeplatform.domain.repository

import com.yolbertdev.coffeeplatform.domain.model.User

interface UserRepository {
    suspend fun login(username: String, password: String): User?
    suspend fun register(username: String, password: String, name: String): Boolean
}