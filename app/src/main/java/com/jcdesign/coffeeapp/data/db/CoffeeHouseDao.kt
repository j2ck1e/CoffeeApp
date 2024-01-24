package com.jcdesign.coffeeapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem

@Dao
interface CoffeeHouseDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsertLocations(listOfLocations: LocationResponse)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsertOrder(listOfMenu: List<MenuResponseItem>)

        @Update
        suspend fun addDistance(item: LocationResponseItem)

        @Update
        suspend fun changeCount(item: MenuResponseItem)

        @Query("SELECT * FROM coffeehouse")
        fun getLocationResponse(): LiveData<List<LocationResponseItem>>

        @Query("SELECT * FROM menu")
        fun getOrder(): LiveData<List<MenuResponseItem>>

        @Query("DELETE FROM coffeehouse")
        suspend fun clearData()

        @Query("DELETE FROM menu")
        suspend fun clearOrder()


}