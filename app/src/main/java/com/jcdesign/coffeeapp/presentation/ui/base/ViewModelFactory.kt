package com.jcdesign.coffeeapp.presentation.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcdesign.coffeeapp.data.repository.AuthRepository
import com.jcdesign.coffeeapp.domain.BaseRepository
import com.jcdesign.coffeeapp.presentation.ui.auth.AuthViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}