package com.jcdesign.coffeeapp.data.network.response

data class LoginResponse(
    val token: String,
    val tokenLifetime: Int
)