package com.jcdesign.coffeeapp.presentation.ui.location

import com.jcdesign.coffeeapp.domain.LocationRepository
import com.jcdesign.coffeeapp.presentation.ui.base.BaseViewModel

class MapViewModel(
    private val repository: LocationRepository
) : BaseViewModel(repository) {


    fun getDataFromDB() =
        repository.getSavedLocationResponse()


}