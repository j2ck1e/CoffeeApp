package com.jcdesign.coffeeapp.presentation.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.Resource
import com.jcdesign.coffeeapp.data.network.response.menu.MenuResponseItem
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.databinding.FragmentMenuBinding
import com.jcdesign.coffeeapp.presentation.ui.adapters.MenuInfoAdapter
import com.jcdesign.coffeeapp.presentation.ui.adapters.MenuInfoViewHolder
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.jcdesign.coffeeapp.presentation.ui.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class MenuFragment :
    BaseFragment<MenuViewModel, FragmentMenuBinding, LocationRepository>() {

    private lateinit var adapter: MenuInfoAdapter
    private val args by navArgs<MenuFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)
        adapter = MenuInfoAdapter()
        binding.rvMenuList.adapter = adapter

        viewModel.getMenu(args.id)

        viewModel.menu.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    adapter.submitList(it.value.toList())
                    binding.progressbar.visible(false)
                    adapter.onMenuClickListener = object : MenuInfoAdapter.OnMenuClickListener{
                        override fun onMenuClick(menu: MenuResponseItem) {
                            TODO("Not yet implemented")
                        }

                        override fun onBtnMinusClick(holder: MenuInfoViewHolder) {
                            holder.binding.tvCount.text = viewModel.decrCount(holder)
                        }

                        override fun onBtnPlusClick(holder: MenuInfoViewHolder) {
                           holder.binding.tvCount.text = viewModel.incrCount(holder)
                        }

                    }
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
//            logout()
        }

    }

    override fun getViewModel() = MenuViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMenuBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): LocationRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(LocationApi::class.java, token)
        return LocationRepository(api)
    }

}