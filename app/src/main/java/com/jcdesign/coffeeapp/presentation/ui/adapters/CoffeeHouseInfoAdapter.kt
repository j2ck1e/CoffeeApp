package com.jcdesign.coffeeapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
import com.jcdesign.coffeeapp.databinding.ItemCoffeehouseInfoBinding

class CoffeeHouseInfoAdapter() :
    ListAdapter<LocationResponseItem, CoffeeHouseInfoViewHolder>(CoffeeHouseInfoDiffCallback) {


    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeHouseInfoViewHolder {
        val binding =
            ItemCoffeehouseInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoffeeHouseInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoffeeHouseInfoViewHolder, position: Int) {
        val coffeeHouse = getItem(position)
        with(holder.binding) {
            tvName.text = coffeeHouse.name
            tvDistance.text = " км от Вас"
            root.setOnClickListener {
                onCoinClickListener?.onCoffeeHouseClick(coffeeHouse)
            }
        }
    }

    interface OnCoinClickListener {
        fun onCoffeeHouseClick(coffeeHouse: LocationResponseItem)
    }

}