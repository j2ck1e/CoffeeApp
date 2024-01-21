package com.jcdesign.coffeeapp.presentation.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jcdesign.coffeeapp.R
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



        viewModel.registerResponse.observe(viewLifecycleOwner, Observer {

            if (it is Resource.Success) {
                lifecycleScope.launch {
                    viewModel.saveAuthToken(it.value.token!!)
                    requireActivity().startNewActivity(LocationActivity::class.java)

                }
            } else if (it is Resource.Failure) handleApiError(it) { }
        })


        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEmail(binding.etEmail, binding.tilEmail)
            }

        })


        binding.etFirstPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validatePassword(binding.etFirstPass, binding.tilFirstPass)
            }

        })


        binding.etSecondPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateConPassword(binding.etFirstPass, binding.etSecondPass, binding.tilSecondPass)
            }

        })



        binding.regBtn.setOnClickListener {
            if (validateEmail(binding.etEmail, binding.tilEmail) && validatePassword(
                    binding.etFirstPass,
                    binding.tilFirstPass
                ) && validateConPassword(
                    binding.etFirstPass,
                    binding.etSecondPass,
                    binding.tilSecondPass
                )
            ) {

                register()

            }

        }
    }

    private fun validateEmail(etEmail: TextInputEditText, tilEmail: TextInputLayout): Boolean {
        val emailPattern = Regex("[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+")
        return when {
            etEmail.text.toString().trim().isEmpty() -> {
                tilEmail.error = "Required"
                false
            }

            !etEmail.text.toString().trim().matches(emailPattern) -> {
                tilEmail.error = "Valid E-mail"
                false
            }

            else -> {
                tilEmail.error = null
                true
            }
        }
    }


    private fun validatePassword(
        etFirstPass: TextInputEditText,
        tilFirstPass: TextInputLayout,
    ): Boolean {
        return when {
            etFirstPass.text.toString().trim().isEmpty() -> {
                tilFirstPass.error = "Required"
                false
            }

            etFirstPass.text.toString().trim().length < 3 || etFirstPass.text.toString()
                .trim().length > 10 -> {
                tilFirstPass.error = "Password must be 3 to 10 Character!"
                false
            }

            else -> {
                tilFirstPass.error = null
                true
            }
        }
    }


    private fun validateConPassword(
        etFirstPass: TextInputEditText,
        etSecondPass: TextInputEditText,
        tilSecondPass: TextInputLayout,
    ): Boolean {

        return when {
            etSecondPass.text.toString().trim().isEmpty() -> {
                tilSecondPass.error = "Required"
                false
            }

            etSecondPass.text.toString().trim().length < 3 || etSecondPass.text.toString()
                .trim().length > 10 -> {
                tilSecondPass.error = "Password must be 3 to 10 Character!"
                false

            }

            etFirstPass.text.toString().trim() != etSecondPass.text.toString().trim() -> {
                tilSecondPass.error = "Password Don't Match!"
                false
            }

            else -> {
                tilSecondPass.error = null
                true
            }
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
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)
}