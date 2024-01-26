package com.jcdesign.coffeeapp.presentation.ui.adapters.coffeehouse

import androidx.recyclerview.widget.DiffUtil
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem

object CoffeeHouseInfoDiffCallback: DiffUtil.ItemCallback<LocationResponseItem>() {

    override fun areItemsTheSame(oldItem: LocationResponseItem, newItem: LocationResponseItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationResponseItem, newItem: LocationResponseItem): Boolean {
        return oldItem == newItem
    }
}