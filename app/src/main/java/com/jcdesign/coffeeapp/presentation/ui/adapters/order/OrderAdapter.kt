package com.jcdesign.coffeeapp.presentation.ui.adapters.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
import com.jcdesign.coffeeapp.databinding.ItemOrderBinding

class OrderAdapter :
    ListAdapter<MenuResponseItem, OrderInfoViewHolder>(OrderInfoDiffCallback) {


    var onMenuClickListener: OnMenuClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderInfoViewHolder {
        val binding =
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderInfoViewHolder, position: Int) {
        val order = getItem(position)
        with(holder.binding) {
            tvName.text = order.name
            tvPrice.text = order.price.toString() + " руб."
            tvCount.text = order.count.toString()
            btnMinus.setOnClickListener {
                onMenuClickListener?.onBtnMinusClick(order)

            }
            btnPlus.setOnClickListener {
                onMenuClickListener?.onBtnPlusClick(order)


            }

        }
    }

    interface OnMenuClickListener {
        fun onBtnMinusClick(order: MenuResponseItem)
        fun onBtnPlusClick(order: MenuResponseItem)
    }


}