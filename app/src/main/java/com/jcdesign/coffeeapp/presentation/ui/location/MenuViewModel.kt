package com.jcdesign.coffeeapp.presentation.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponse
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: LocationRepository
) : BaseViewModel(repository) {

    private val _menu: MutableLiveData<Resource<MenuResponse>> = MutableLiveData()
    val menu: LiveData<Resource<MenuResponse>>
        get() = _menu


    fun getMenu(id: String) = viewModelScope.launch {
        _menu.value = Resource.Loading
        _menu.value = repository.getMenu(id)
    }
}