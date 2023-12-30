package com.jcdesign.coffeeapp.presentation.ui.base

import androidx.lifecycle.ViewModel
import com.jcdesign.coffeeapp.data.network.AuthApi
import com.jcdesign.coffeeapp.domain.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    suspend fun logout(api: AuthApi) = withContext(Dispatchers.IO) { repository.logout(api) }
}