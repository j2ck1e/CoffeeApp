package com.jcdesign.coffeeapp.presentation.ui.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.jcdesign.coffeeapp.R
import com.jcdesign.coffeeapp.data.db.CoffeeHouseDatabase
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.databinding.FragmentMenuBinding
import com.jcdesign.coffeeapp.databinding.FragmentOrderBinding
import com.jcdesign.coffeeapp.presentation.ui.adapters.MenuInfoAdapter
import com.jcdesign.coffeeapp.presentation.ui.adapters.MenuInfoViewHolder
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.jcdesign.coffeeapp.presentation.ui.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class OrderFragment : Fragment() {

//    private lateinit var adapter: OrderInfoAdapter
    private lateinit var binding: FragmentOrderBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



    }


}