package com.jcdesign.coffeeapp.presentation.ui.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class LocationViewModel(
    private val context: Context,
    private val repository: LocationRepository
) : BaseViewModel(repository) {

    private val _coffeeHouses: MutableLiveData<Resource<LocationResponse>> = MutableLiveData()
    val coffeeHouses: LiveData<Resource<LocationResponse>>
        get() = _coffeeHouses

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    private val _distance: MutableLiveData<String> = MutableLiveData()
    val distance: LiveData<String>
        get() = _distance

    fun getCoffeeHouses() = viewModelScope.launch {
        Log.d("MyTAG", "getCoffeeHouses...")
        _coffeeHouses.value = Resource.Loading
        val response = repository.getLocation()
        _coffeeHouses.postValue(response)
    }

     suspend fun saveLocationResponse(listOfLocationResponse: LocationResponse) {
        repository.upsert(listOfLocationResponse)
    }

     fun getDataFromDB() =
        repository.getSavedLocationResponse()



     fun clearData() = viewModelScope.launch {
        repository.clearData()
    }

    fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000)  // 10 seconds
            .setFastestInterval(5000)  // 5 seconds

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0.lastLocation?.let {
                    _currentLocation.postValue(it)
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

//    fun stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//
//    private val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(p0: LocationResult) {
//            p0.lastLocation?.let {
//                _currentLocation.postValue(it)
//            }
//        }
//    }



}