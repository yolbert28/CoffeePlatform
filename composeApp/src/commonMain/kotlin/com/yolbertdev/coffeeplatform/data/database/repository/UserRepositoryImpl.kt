package com.yolbertdev.coffeeplatform.data.database.repository

import com.yolbertdev.coffeeplatform.db.CoffeeDatabase
import com.yolbertdev.coffeeplatform.domain.model.User
import com.yolbertdev.coffeeplatform.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val db: CoffeeDatabase
) : UserRepository {

    override suspend fun login(username: String, password: String): User? = withContext(Dispatchers.IO) {
        val dbUser = db.userQueries.login(username, password).executeAsOneOrNull()
        dbUser?.let {
            User(it.id, it.username, it.name)
        }
    }

    override suspend fun register(username: String, password: String, name: String): Boolean = withContext(Dispatchers.IO) {
        // Verificar si existe
        val existing = db.userQueries.selectByUsername(username).executeAsOneOrNull()
        if (existing != null) return@withContext false

        db.userQueries.insert(username, password, name)
        return@withContext true
    }
}