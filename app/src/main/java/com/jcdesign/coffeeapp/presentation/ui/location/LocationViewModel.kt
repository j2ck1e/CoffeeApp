package com.jcdesign.coffeeapp.presentation.ui.location

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
    val coffeeHouses: LiveData<Resource<LocationResponse>>
        get() = _coffeeHouses

    private val _distance: MutableLiveData<String> = MutableLiveData()
    val distance: LiveData<String>
        get() = _distance

    fun getCoffeeHouses() = viewModelScope.launch {
        _coffeeHouses.value = Resource.Loading
        _coffeeHouses.value = repository.getLocation()
    }

    fun setDistance():
            LiveData<String> {
        _distance.value = "3"
        return distance
    }

}