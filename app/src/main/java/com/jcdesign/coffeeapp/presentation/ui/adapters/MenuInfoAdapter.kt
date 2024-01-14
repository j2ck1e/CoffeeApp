package com.jcdesign.coffeeapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
import com.jcdesign.coffeeapp.databinding.ItemMenuInfoBinding

class MenuInfoAdapter :
    ListAdapter<MenuResponseItem, MenuInfoViewHolder>(MenuInfoDiffCallback) {


    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuInfoViewHolder {
        val binding =
            ItemMenuInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuInfoViewHolder, position: Int) {
        val menu = getItem(position)
        with(holder.binding) {
            Glide.with(holder.itemView).load(menu.imageURL).into(ivDrink)
            tvDrinkName.text = menu.name
            tvPrice.text = menu.price.toString()
            root.setOnClickListener {
                onCoinClickListener?.onMenuClick(menu)
            }
        }
    }

    interface OnCoinClickListener {
        fun onMenuClick(menu: MenuResponseItem)
    }
}