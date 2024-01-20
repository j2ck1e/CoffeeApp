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
import com.jcdesign.coffeeapp.presentation.ui.enable
import com.jcdesign.coffeeapp.presentation.ui.visible
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt


class LocationFragment :
    BaseFragment<LocationViewModel, FragmentLocationBinding, LocationRepository>() {

    private lateinit var adapter: CoffeeHouseInfoAdapter
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var points: List<LocationResponseItem>
    private lateinit var myLocation: Point


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.enable(false)

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
                LocationFragmentDirections.actionHomeFragmentToMapFragment(
                    lon = myLocation.longitude
                        .toString(),
                    lat = myLocation.latitude.toString(),
                )
            )
        }

    }

    private fun addDistance(oldItem: LocationResponseItem, distance: String) {
        val newItem = oldItem.copy(distance = distance)
        lifecycleScope.launch {
            viewModel.addDistance(newItem)
        }
    }

    private fun getCurrentLocation() {
        Log.d("MyTAG", "getCurrentLocation...")
        viewModel.requestLocationUpdates()
        viewModel.currentLocation.observe(viewLifecycleOwner, Observer { it ->
            Log.d("MyTAG", "getCurrentLocation: ${it.latitude}, ${it.longitude}")
            myLocation = Point(it.latitude, it.longitude)
        })
        viewModel.getCoffeeHouses()

    }

    fun calculateDistance(myLocation: Point, targetLocation: Point): String {

        val earthRadius = 6371
        var lat1 = myLocation.latitude
        var lat2 = targetLocation.latitude
        var lon1 = myLocation.longitude
        var lon2 = targetLocation.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        val a = Math.pow(Math.sin(dLat / 2), 2.0) + Math.pow(
            Math.sin(dLon / 2),
            2.0
        ) * Math.cos(lat1) * Math.cos(lat2)
        val c = 2 * Math.asin(Math.sqrt(a))
        val result = earthRadius * c
        return String.format("%.2f".format(result))

    }

    private fun complex(response: LocationResponse) {

        lifecycleScope.launch {
            viewModel.saveLocationResponse(response)
            for (i in 0 until response.size) {
                val dist = calculateDistance(
                    myLocation, Point(
                        response[i].point.latitude.toDouble(), response[i]
                            .point
                            .longitude.toDouble()
                    )
                )
                addDistance(response[i], dist)
                Log.d("MyTAG", "dist: $dist")
            }




            viewModel.getDataFromDB().observe(viewLifecycleOwner, Observer { listOfData ->
//
                adapter.submitList(listOfData)
                points = listOfData.map {
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
                binding.btnLogout.enable(true)

            }

//            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
//                Toast.makeText(
//                    requireContext(), "Включите геолокацию", Toast.LENGTH_LONG
//                )
//                    .show()
//                pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//            }

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
                binding.btnLogout.enable(true)

            } else {

                Toast.makeText(
                    requireContext(), "Для корректной работы приложения необходимо разрешение на" +
                            " получение геолокации пользователя", Toast.LENGTH_LONG
                )
                    .show()
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