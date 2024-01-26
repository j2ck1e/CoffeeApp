package com.jcdesign.coffeeapp.presentation.ui.adapters.menu

import androidx.recyclerview.widget.DiffUtil
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem

object MenuInfoDiffCallback : DiffUtil.ItemCallback<MenuResponseItem>() {

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