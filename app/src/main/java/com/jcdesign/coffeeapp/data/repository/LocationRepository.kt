package com.jcdesign.coffeeapp.data.repository

import android.app.Application
import com.jcdesign.coffeeapp.data.db.CoffeeHouseDatabase
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.domain.BaseRepository

class LocationRepository(
    val db: CoffeeHouseDatabase,
    private val api: LocationApi
) : BaseRepository() {

    suspend fun getLocation() = safeApiCall {
        api.getLocation()
    }

    suspend fun getMenu(id: String) = safeApiCall {
        api.getMenu(id)
    }

    suspend fun upsert(listOfLocationResponse: LocationResponse) =
        db.getCoffeeHouseDao().upsert(listOfLocationResponse)


    fun getSavedLocationResponse() = db.getCoffeeHouseDao().getLocationResponse()

    suspend fun clearData() {
        db.getCoffeeHouseDao().clearData()
    }
}