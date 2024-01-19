package com.jcdesign.coffeeapp.presentation.ui.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.presentation.ui.base.BaseViewModel

class MapViewModel(
    private val repository: LocationRepository
) : BaseViewModel(repository) {


    fun getDataFromDB() =
        repository.getSavedLocationResponse()


}