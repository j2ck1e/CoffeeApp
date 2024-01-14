package com.jcdesign.coffeeapp.presentation.ui.location

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponse
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.presentation.ui.adapters.MenuInfoViewHolder
import com.jcdesign.coffeeapp.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: LocationRepository
) : BaseViewModel(repository) {

    private var _count: MutableLiveData<Int> = MutableLiveData()
    val count: LiveData<Int>
        get() = _count

    private val _menu: MutableLiveData<Resource<MenuResponse>> = MutableLiveData()
    val menu: LiveData<Resource<MenuResponse>>
        get() = _menu


    fun getMenu(id: String) = viewModelScope.launch {
        _menu.value = Resource.Loading
        _menu.value = repository.getMenu(id)
    }

    fun incrCount(holder: MenuInfoViewHolder): String {
        _count.value = holder.binding.tvCount.text.toString().toInt()
        return if(_count.value!! < 10) {
            (_count.value!! + 1).toString()
        } else{
            _count.value.toString()
        }
    }

    fun decrCount(holder: MenuInfoViewHolder): String {
        _count.value = holder.binding.tvCount.text.toString().toInt()
        return if(_count.value!! > 0) {
            (_count.value!! - 1).toString()
        } else{
            _count.value.toString()
        }
    }

}