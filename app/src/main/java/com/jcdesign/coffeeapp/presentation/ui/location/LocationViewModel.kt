package com.jcdesign.coffeeapp.presentation.ui.location

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationRepository
) : BaseViewModel(repository) {

    private val _coffeeHouses: MutableLiveData<Resource<LocationResponse>> = MutableLiveData()
    val location: LiveData<Resource<LocationResponse>>
        get() = _coffeeHouses

    fun getCoffeeHouses() = viewModelScope.launch {
        _coffeeHouses.value = Resource.Loading
        _coffeeHouses.value = repository.getLocation()
    }



    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = lon1
        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = lon2
        return (loc1.distanceTo(loc2) / 1000).toString()
    }
}