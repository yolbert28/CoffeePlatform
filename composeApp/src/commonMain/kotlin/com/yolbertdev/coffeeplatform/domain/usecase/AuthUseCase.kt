package com.yolbertdev.coffeeplatform.domain.usecase

import com.yolbertdev.coffeeplatform.domain.model.User
import com.yolbertdev.coffeeplatform.domain.repository.UserRepository

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(username: String, pass: String): User? {
        return repository.login(username, pass)
    }
}

class RegisterUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(username: String, pass: String, name: String): Boolean {
        return repository.register(username, pass, name)
    }
}