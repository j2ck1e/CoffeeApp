package com.jcdesign.coffeeapp.data.network.response.location

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "coffeehouse")
data class LocationResponseItem(
    @PrimaryKey
    var id: Int,
    var name: String = "",
    var point: Point,
    var distance: String? = ""
)