package com.jcdesign.coffeeapp.presentation.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.LoginResponse
import com.jcdesign.coffeeapp.data.network.response.RegisterResponse
import com.jcdesign.coffeeapp.data.repository.AuthRepository
import com.jcdesign.coffeeapp.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.Response

class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {


    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    private val _registerResponse: MutableLiveData<Resource<RegisterResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    val registerResponse: LiveData<Resource<RegisterResponse>>
        get() = _registerResponse

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password)
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        _registerResponse.value = Resource.Loading
        _registerResponse.value = repository.register(email, password)
    }

    suspend fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }
}