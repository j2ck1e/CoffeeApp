package com.jcdesign.coffeeapp.presentation.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jcdesign.coffeeapp.data.db.CoffeeHouseDatabase
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.databinding.FragmentLocationBinding
import com.jcdesign.coffeeapp.presentation.ui.adapters.CoffeeHouseInfoAdapter
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.jcdesign.coffeeapp.presentation.ui.visible
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LocationFragment :
    BaseFragment<LocationViewModel, FragmentLocationBinding, LocationRepository>() {
    private lateinit var adapter: CoffeeHouseInfoAdapter
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var points: List<LocationResponseItem>
    private lateinit var myLocation: Point
    private lateinit var targetLocation: Point


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerPermissionListener()
        checkLocationPermission()


        binding.progressbar.visible(false)
        adapter = CoffeeHouseInfoAdapter(viewModel)
        binding.rvCoffeeHouseList.adapter = adapter



        viewModel.coffeeHouses.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.value.let {
                        binding.progressbar.visible(false)
                        viewModel.clearData()
                        complex(it)

                    }
                    adapter.onItemClickListener =
                        object : CoffeeHouseInfoAdapter.OnItemClickListener {
                            override fun onCoffeeHouseClick(coffeeHouse: LocationResponseItem) {
                                findNavController().navigate(
                                    LocationFragmentDirections.actionHomeFragmentToMenuFragment(
                                        coffeeHouse.id.toString()
                                    )
                                )

                            }

                        }
                }

                is Resource.Loading -> {
                    binding.progressbar.visible(true)

                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Resource.Failure", Toast.LENGTH_SHORT).show()
                    logout()
                }
            }
        })



        binding.btnLogout.setOnClickListener {
            findNavController().navigate(
                LocationFragmentDirections.actionHomeFragmentToMapFragment(lon = myLocation.longitude
                        .toString(),
                    lat = myLocation.latitude.toString(),
                    )
            )
        }


    }

    private fun getCurrentLocation() {
        viewModel.requestLocationUpdates()
        viewModel.currentLocation.observe(viewLifecycleOwner, Observer { it ->
            Log.d("MyTAG", "getCurrentLocation: ${it.latitude}, ${it.longitude}")
            myLocation = Point(it.latitude, it.longitude)
        })


    }

    fun calculateDistance(myLocation: Point, targetLocation: Point): Double {

        // TODO: result
        return 3.14
    }

    private fun complex(response: LocationResponse) {
        lifecycleScope.launch {
            viewModel.saveLocationResponse(response)
            viewModel.getDataFromDB().observe(viewLifecycleOwner, Observer { listOfData ->
//
                adapter.submitList(listOfData)
                points = listOfData.map{
                    it
                }
                Log.d("MyTAG", "getPointsFromDB: $points")
            })

        }

    }


    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {

                getCurrentLocation()
                viewModel.getCoffeeHouses()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(requireContext(), "We need your permission", Toast.LENGTH_SHORT)
                    .show()
                pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                getCurrentLocation()
                viewModel.getCoffeeHouses()
                Toast.makeText(requireContext(), "Location was activated", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun getViewModel() = LocationViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLocationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): LocationRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(LocationApi::class.java, token)
        val db = CoffeeHouseDatabase.invoke(requireContext())
        return LocationRepository(db, api)
    }


}