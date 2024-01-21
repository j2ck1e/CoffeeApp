package com.jcdesign.coffeeapp.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jcdesign.coffeeapp.R
import com.jcdesign.coffeeapp.data.network.AuthApi
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.repository.AuthRepository
import com.jcdesign.coffeeapp.databinding.FragmentLoginBinding
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.jcdesign.coffeeapp.presentation.ui.enable
import com.jcdesign.coffeeapp.presentation.ui.handleApiError
import com.jcdesign.coffeeapp.presentation.ui.location.LocationActivity
import com.jcdesign.coffeeapp.presentation.ui.startNewActivity
import com.jcdesign.coffeeapp.presentation.ui.visible
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressbar.visible(false)
        binding.loginBtn.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                lifecycleScope.launch {
                    viewModel.saveAuthToken(it.value.token!!)
                    requireActivity().startNewActivity(LocationActivity::class.java)

                }
            } else if (it is Resource.Failure) handleApiError(it) {

            }
        })

        binding.etPass.addTextChangedListener {
            val email = binding.etEmail.text.toString().trim()
            binding.loginBtn.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.loginBtn.setOnClickListener {
            login()
        }

        binding.tvRegisterNow.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPass.text.toString().trim()
        viewModel.login(email, password)
    }

    override fun getViewModel() = AuthViewModel::class.java


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

}