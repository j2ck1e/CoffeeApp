package com.jcdesign.coffeeapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _location: MutableLiveData<Resource<LocationResponse>> = MutableLiveData()
    val location: LiveData<Resource<LocationResponse>>
        get() = _location

    fun getLocation() = viewModelScope.launch {
        _location.value = Resource.Loading
        _location.value = repository.getLocation()
    }
}