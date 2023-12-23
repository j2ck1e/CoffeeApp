package com.jcdesign.coffeeapp.data.repository

import com.jcdesign.coffeeapp.data.network.AuthApi
import com.jcdesign.coffeeapp.domain.BaseRepository

class AuthRepository(
    private val api: AuthApi
) : BaseRepository() {

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        api.login(email, password)
    }
}