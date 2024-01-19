package com.jcdesign.coffeeapp.presentation.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcdesign.coffeeapp.data.repository.AuthRepository
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.domain.BaseRepository
import com.jcdesign.coffeeapp.presentation.ui.auth.AuthViewModel
import com.jcdesign.coffeeapp.presentation.ui.location.LocationViewModel
import com.jcdesign.coffeeapp.presentation.ui.location.MapViewModel
import com.jcdesign.coffeeapp.presentation.ui.location.MenuViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val context: Context,
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(LocationViewModel::class.java) -> LocationViewModel(
                context,
                repository as
                        LocationRepository
            ) as T

            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(repository as LocationRepository) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel(repository as LocationRepository) as T

            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}