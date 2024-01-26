package com.jcdesign.coffeeapp.domain

import com.jcdesign.coffeeapp.data.db.CoffeeHouseDatabase
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
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

    suspend fun upsertLocations(listOfLocationResponse: LocationResponse) =
        db.getCoffeeHouseDao().upsertLocations(listOfLocationResponse)

    suspend fun upsertOrder(listOfMenu: List<MenuResponseItem>) =
        db.getCoffeeHouseDao().upsertOrder(listOfMenu)

    suspend fun addDistance(item: LocationResponseItem) = db.getCoffeeHouseDao().addDistance(item)
    suspend fun addCount(item: MenuResponseItem) = db.getCoffeeHouseDao().addCount(item)


    fun getSavedLocationResponse() = db.getCoffeeHouseDao().getLocationResponse()

    fun getMenuDb() = db.getCoffeeHouseDao().getMenuDb()
    fun getOrderDb() = db.getCoffeeHouseDao().getOrderDb()

    fun getOrderById(itemId: Int) = db.getCoffeeHouseDao().getOrderById(itemId)


    suspend fun clearCoffeeHouseDb() = db.getCoffeeHouseDao().clearCoffeeHouseDb()


    suspend fun clearMenuDb() = db.getCoffeeHouseDao().clearMenuDb()

}