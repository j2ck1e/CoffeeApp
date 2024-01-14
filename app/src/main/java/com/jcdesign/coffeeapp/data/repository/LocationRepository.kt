package com.jcdesign.coffeeapp.data.repository

import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.domain.BaseRepository

class LocationRepository(
    private val api: LocationApi
) : BaseRepository() {

    suspend fun getLocation() = safeApiCall {
        api.getLocation()
    }

    suspend fun getMenu(id: String) = safeApiCall {
        api.getMenu(id)
    }
}