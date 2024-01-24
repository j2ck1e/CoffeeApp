package com.jcdesign.coffeeapp.presentation.ui.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jcdesign.coffeeapp.R
import com.jcdesign.coffeeapp.data.db.CoffeeHouseDatabase
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.databinding.FragmentMenuBinding
import com.jcdesign.coffeeapp.presentation.ui.adapters.MenuInfoAdapter
import com.jcdesign.coffeeapp.presentation.ui.adapters.MenuInfoViewHolder
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.jcdesign.coffeeapp.presentation.ui.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MenuFragment :
    BaseFragment<MenuViewModel, FragmentMenuBinding, LocationRepository>() {

    private lateinit var orderList: Array<MenuResponseItem>
//    private var _orderList = mutableListOf<MenuResponseItem>()
    private lateinit var adapter: MenuInfoAdapter
    private val args by navArgs<MenuFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)
        adapter = MenuInfoAdapter()
        binding.rvMenuList.adapter = adapter

        viewModel.getMenu(args.id)



        viewModel.menu.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    viewModel.clearOrder()
                    lifecycleScope.launch { viewModel.upsertOrder(it.value.toList())}
                    viewModel.order.observe(viewLifecycleOwner, Observer { orderList ->
                        adapter.submitList(orderList)
                    })
                    binding.progressbar.visible(false)
                    adapter.onMenuClickListener = object : MenuInfoAdapter.OnMenuClickListener {
                        override fun onMenuClick(menu: MenuResponseItem) {
                            TODO("Not yet implemented")
                        }

                        override fun onBtnMinusClick(holder: MenuInfoViewHolder) {
                            holder.binding.tvCount.text = viewModel.decrCount(holder)
                        }

                        override fun onBtnPlusClick(holder: MenuInfoViewHolder) {

                            holder.binding.tvCount.text = viewModel.incrCount(holder)
//                            if (holder.binding.tvCount.text != "0") {
//                                val item = it.value[holder.adapterPosition]
//
//                                _orderList.add(
//                                    MenuResponseItem(
//                                        item.id, item.imageURL, item.name,
//                                        item.price, holder.binding.tvCount.text.toString().toInt()
//                                    )
//                                )
//
//                            }
//                            orderList = _orderList.toTypedArray()
//                            Log.d("MyTAG", "args: ${orderList.toList()}, size: ${orderList.size}")
                        }
                    }

                }



            is Resource.Loading -> {
            binding.progressbar.visible(true)

        }

            is Resource.Failure -> {
            Toast.makeText(requireContext(), "Resource.Failure", Toast.LENGTH_SHORT).show()
        }
        }
    })

    binding.btnToOrder.setOnClickListener()
    {

        findNavController().navigate(R.id.action_menuFragment_to_orderFragment)
    }

}

override fun getViewModel() = MenuViewModel::class.java

override fun getFragmentBinding(
    inflater: LayoutInflater,
    container: ViewGroup?
) = FragmentMenuBinding.inflate(inflater, container, false)

override fun getFragmentRepository(): LocationRepository {
    val token = runBlocking { userPreferences.authToken.first() }
    val api = remoteDataSource.buildApi(LocationApi::class.java, token)
    val db = CoffeeHouseDatabase.invoke(requireContext())
    return LocationRepository(db, api)
}

}