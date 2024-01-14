package com.jcdesign.coffeeapp.presentation.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponse
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.databinding.FragmentLocationBinding
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.jcdesign.coffeeapp.presentation.ui.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class LocationFragment : BaseFragment<LocationViewModel, FragmentLocationBinding, LocationRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.getLocation()

        viewModel.location.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->{
                    binding.progressbar.visible(false)
                    updateUI(it.value)
                }

                is Resource.Loading -> {
                    binding.progressbar.visible(true)

                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Resource.Failure", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }



    private fun updateUI(locationResponse: LocationResponse) {
        with(binding){
//            tvId.text = locationResponse[0].id.toString()
//            tvName.text = locationResponse[0].name
        }

    }

    override fun getViewModel() = LocationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLocationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): LocationRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(LocationApi::class.java, token)
        return LocationRepository(api)
    }


}