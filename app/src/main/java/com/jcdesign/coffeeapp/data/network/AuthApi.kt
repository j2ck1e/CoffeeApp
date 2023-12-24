package com.jcdesign.coffeeapp.data.network

import com.jcdesign.coffeeapp.data.network.request.LoginRequest
import com.jcdesign.coffeeapp.data.network.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {


    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : LoginResponse
}