package com.jcdesign.coffeeapp.data.network.response.menu

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


@Entity(tableName = "menu")
data class MenuResponseItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val imageURL: String,
    var name: String,
    var price: Int,
    var count: Int = 0
) : Serializable