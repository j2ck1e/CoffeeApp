package com.jcdesign.coffeeapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem

@Dao
interface CoffeeHouseDao {



        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsert(listOfDetailWeather: LocationResponse)/*: List<Long>*/

        @Update
        suspend fun addDistance(item: LocationResponseItem)

        @Query("SELECT * FROM coffeehouse")
        fun getLocationResponse(): LiveData<List<LocationResponseItem>>

//        @Query("SELECT point FROM coffeehouse")
//        fun getPoints(): LiveData<List<LocationResponseItem>>


        @Query("DELETE FROM coffeehouse")
        suspend fun clearData()


}