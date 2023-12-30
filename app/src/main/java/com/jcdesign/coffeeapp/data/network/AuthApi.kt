package com.jcdesign.coffeeapp.data.network

import com.jcdesign.coffeeapp.data.network.request.LoginRequest
import com.jcdesign.coffeeapp.data.network.response.LoginResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {


    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : LoginResponse

//    @POST("logout")
    suspend fun logout(): ResponseBody
}