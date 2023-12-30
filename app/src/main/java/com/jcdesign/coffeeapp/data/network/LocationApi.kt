package com.jcdesign.coffeeapp.data.network

import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import retrofit2.http.GET

interface LocationApi {

    @GET("locations")
    suspend fun getLocation(): LocationResponse
}