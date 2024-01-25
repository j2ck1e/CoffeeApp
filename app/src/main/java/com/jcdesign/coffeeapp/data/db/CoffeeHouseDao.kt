package com.jcdesign.coffeeapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem

@Dao
interface CoffeeHouseDao {


    @Upsert
    suspend fun upsertLocations(listOfLocations: LocationResponse)

    @Upsert
    suspend fun upsertOrder(listOfMenu: List<MenuResponseItem>)

    @Update
    suspend fun addDistance(item: LocationResponseItem)

    @Update
    suspend fun addCount(item: MenuResponseItem)

    @Query("SELECT * FROM coffeehouse")
    fun getLocationResponse(): LiveData<List<LocationResponseItem>>

    @Query("SELECT * FROM menu")
    fun getMenuDb(): LiveData<List<MenuResponseItem>>

    @Query("SELECT * FROM menu WHERE id=:itemId")
    fun getOrderById(itemId: Int): MenuResponseItem

    @Query("DELETE FROM coffeehouse")
    suspend fun clearData()

    @Query("DELETE FROM menu")
    suspend fun clearOrder()


}