package com.jcdesign.coffeeapp.data.network.response.location

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "coffeehouse")
data class LocationResponseItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String = "",
    var point: Point? = null,
    var distance: Float? = null
): Serializable