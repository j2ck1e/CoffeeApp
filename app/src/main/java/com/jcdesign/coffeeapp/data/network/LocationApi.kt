package com.jcdesign.coffeeapp.data.network

import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationApi {

    @GET("locations")
    suspend fun getLocation(): LocationResponse

    @GET("/location/{id}/menu")
    suspend fun getMenu(@Path("id") coffeeHouseId: String): MenuResponse
}