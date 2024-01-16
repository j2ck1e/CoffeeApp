package com.jcdesign.coffeeapp.presentation.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.databinding.FragmentLocationBinding
import com.jcdesign.coffeeapp.presentation.ui.adapters.CoffeeHouseInfoAdapter
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.jcdesign.coffeeapp.presentation.ui.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class LocationFragment :
    BaseFragment<LocationViewModel, FragmentLocationBinding, LocationRepository>() {
    private lateinit var adapter: CoffeeHouseInfoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)
        adapter = CoffeeHouseInfoAdapter(viewModel)
        binding.rvCoffeeHouseList.adapter = adapter

        viewModel.getCoffeeHouses()

        viewModel.coffeeHouses.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    adapter.submitList(it.value.toList())
                    binding.progressbar.visible(false)
                    adapter.onItemClickListener = object : CoffeeHouseInfoAdapter.OnItemClickListener{
                        override fun onCoffeeHouseClick(coffeeHouse: LocationResponseItem) {
                            findNavController().navigate(LocationFragmentDirections.actionHomeFragmentToMenuFragment(coffeeHouse.id.toString()))

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

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }



    override fun getViewModel() = LocationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLocationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): LocationRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(LocationApi::class.java, token)
        return LocationRepository(api)
    }


}