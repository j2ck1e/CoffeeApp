package com.jcdesign.coffeeapp.presentation.ui.adapters.order

import androidx.recyclerview.widget.DiffUtil
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem

object OrderInfoDiffCallback : DiffUtil.ItemCallback<MenuResponseItem>() {

    override fun areItemsTheSame(
        oldItem: MenuResponseItem,
        newItem: MenuResponseItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MenuResponseItem,
        newItem: MenuResponseItem
    ): Boolean {
        return oldItem == newItem
    }
}