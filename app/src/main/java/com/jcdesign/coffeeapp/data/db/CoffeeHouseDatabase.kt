package com.jcdesign.coffeeapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.data.network.response.location.Point
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem

@Database(
    entities = [LocationResponseItem::class, MenuResponseItem::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class CoffeeHouseDatabase: RoomDatabase() {

    abstract fun getCoffeeHouseDao(): CoffeeHouseDao

    companion object{
        @Volatile
        private var instance: CoffeeHouseDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CoffeeHouseDatabase::class.java,
                "coffeehouse_db.db"
            ).build()
    }

}