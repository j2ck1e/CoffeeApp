package com.jcdesign.coffeeapp.presentation.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.jcdesign.coffeeapp.data.db.CoffeeHouseDatabase
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
import com.jcdesign.coffeeapp.domain.LocationRepository
import com.jcdesign.coffeeapp.databinding.FragmentOrderBinding
import com.jcdesign.coffeeapp.presentation.ui.adapters.order.OrderAdapter
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class OrderFragment : BaseFragment<MenuViewModel, FragmentOrderBinding, LocationRepository>() {


    private lateinit var adapter: OrderAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = OrderAdapter()
        binding.rvOrderList.adapter = adapter

        viewModel.getOrderDb().observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })

        adapter.onMenuClickListener = object : OrderAdapter.OnMenuClickListener {

            override fun onBtnMinusClick(order: MenuResponseItem) {

                viewModel.decrCount(order.id)

            }

            override fun onBtnPlusClick(order: MenuResponseItem) {

                viewModel.incrCount(order.id)

            }
        }
        
        binding.btnToOrder.setOnClickListener {
            Toast.makeText(requireContext(), "К оплате", Toast.LENGTH_SHORT).show()
        }

    }


    override fun getViewModel(): Class<MenuViewModel> = MenuViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentOrderBinding
        .inflate(inflater, container, false)

    override fun getFragmentRepository(): LocationRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(LocationApi::class.java, token)
        val db = CoffeeHouseDatabase.invoke(requireContext())
        return LocationRepository(db, api)
    }


}