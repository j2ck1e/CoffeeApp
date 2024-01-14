package com.jcdesign.coffeeapp.data.network.response

data class RegisterResponse(
    val token: String?,
    val tokenLifetime: Int
)
