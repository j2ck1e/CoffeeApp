package com.jcdesign.coffeeapp.presentation.ui.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.domain.LocationRepository
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

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0.lastLocation?.let {
                _currentLocation.postValue(it)
            }
        }
    }


    fun getCoffeeHouses() = viewModelScope.launch {
        _coffeeHouses.value = Resource.Loading
        val response = repository.getLocation()
        _coffeeHouses.postValue(response)
    }

    suspend fun saveLocationResponse(listOfLocationResponse: LocationResponse) {
        repository.upsertLocations(listOfLocationResponse)
    }

    suspend fun addDistance(item: LocationResponseItem) {
        repository.addDistance(item)
    }

    fun getDataFromDB() = repository.getSavedLocationResponse()


    fun clearCoffeeHouseDb() = viewModelScope.launch {
        repository.clearCoffeeHouseDb()
    }

    fun requestLocationUpdates() {
        val locationRequest = LocationRequest
            .Builder(PRIORITY_HIGH_ACCURACY, 10000).build()

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun stopLocationUpdates() {

        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}