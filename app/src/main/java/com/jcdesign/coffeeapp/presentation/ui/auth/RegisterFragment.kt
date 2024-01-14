package com.jcdesign.coffeeapp.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.jcdesign.coffeeapp.data.network.AuthApi
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.repository.AuthRepository
import com.jcdesign.coffeeapp.databinding.FragmentRegisterBinding
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.jcdesign.coffeeapp.presentation.ui.enable
import com.jcdesign.coffeeapp.presentation.ui.handleApiError
import com.jcdesign.coffeeapp.presentation.ui.location.LocationActivity
import com.jcdesign.coffeeapp.presentation.ui.startNewActivity
import kotlinx.coroutines.launch


class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding, AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {

            if (it is Resource.Success) {
                lifecycleScope.launch {
                    viewModel.saveAuthToken(it.value.token!!)
                    requireActivity().startNewActivity(LocationActivity::class.java)

                }
            } else if (it is Resource.Failure) handleApiError(it) {register()}
        })

        binding.etSecondPass.addTextChangedListener {
            val email = binding.etEmail.text.toString().trim()
            binding.regBtn.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.regBtn.setOnClickListener {
            register()
        }

    }

    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etSecondPass.text.toString().trim()
        viewModel.register(email, password)
        viewModel.login(email, password)
    }

    override fun getViewModel() = AuthViewModel::class.java


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)
}