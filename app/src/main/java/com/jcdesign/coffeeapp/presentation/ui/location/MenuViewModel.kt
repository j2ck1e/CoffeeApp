package com.jcdesign.coffeeapp.presentation.ui.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponse
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    suspend fun upsertOrder(listOfMenu: List<MenuResponseItem>) = viewModelScope.launch {
        repository.upsertOrder(listOfMenu)
    }

    fun getMenuDb() = repository.getMenuDb()

    fun incrCount(itemId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val old = repository.getOrderById(itemId)
            if (old.count < 10) {
                val newCount = ++old.count
                val new = old.copy(count = newCount)
                repository.addCount(new)
            }
        }
    }

    fun decrCount(itemId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val old = repository.getOrderById(itemId)
            if (old.count > 0) {
                val newCount = --old.count
                val new = old.copy(count = newCount)
                repository.addCount(new)
            }
        }
    }
}