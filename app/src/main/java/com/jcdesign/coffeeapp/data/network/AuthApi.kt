package com.jcdesign.coffeeapp.data.network

import com.jcdesign.coffeeapp.data.network.request.LoginRequest
import com.jcdesign.coffeeapp.data.network.request.RegisterRequest
import com.jcdesign.coffeeapp.data.network.response.LoginResponse
import com.jcdesign.coffeeapp.data.network.response.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {


    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ) : RegisterResponse

    suspend fun logout(): ResponseBody
}