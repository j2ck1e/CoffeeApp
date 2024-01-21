package com.jcdesign.coffeeapp.presentation.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.jcdesign.coffeeapp.R
import com.jcdesign.coffeeapp.data.db.CoffeeHouseDatabase
import com.jcdesign.coffeeapp.data.network.LocationApi
import com.jcdesign.coffeeapp.data.network.response.location.LocationResponseItem
import com.jcdesign.coffeeapp.data.repository.LocationRepository
import com.jcdesign.coffeeapp.databinding.FragmentMapBinding
import com.jcdesign.coffeeapp.presentation.ui.base.BaseFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.TextStyle
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding, LocationRepository>() {

    private val args by navArgs<MapFragmentArgs>()

    private lateinit var data: List<LocationResponseItem>
    private lateinit var points: List<Point>
    private lateinit var names: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.initialize(requireContext())

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val map = binding.mapview.mapWindow.map
        viewModel.getDataFromDB().observe(viewLifecycleOwner, Observer { listOfData ->
            data = listOfData.map {
                it
            }
            getPoints()

            val pinsCollection = map.mapObjects.addCollection()
            val imageProvider = ImageProvider.fromResource(requireContext(), R.mipmap.coffee_cup_location)


            setPoints(pinsCollection, imageProvider)

        })



        map.move(
            CameraPosition(getCurrentLocation(), 12.0f, 0.0f, 30.0f), Animation(
                Animation.Type
                    .SMOOTH, 10f
            ), null
        )


    }

    private fun getPoints() {
        val _points = mutableListOf<Point>()
        val _names = mutableListOf<String>()
        for (point in data) {
            _points.add(Point(point.point.latitude.toDouble(), point.point.longitude.toDouble()))
            _names.add(point.name)
            points = _points.toList()
            names = _names.toList()
        }
    }

    private fun setPoints(
        pinsCollection: MapObjectCollection,
        imageProvider: ImageProvider
    ) {
        for (i in points.indices) {
            pinsCollection.addPlacemark().apply {
                geometry = Point(points[i].latitude, points[i].longitude)
                setIcon(imageProvider)
                setText(
                    names[i],
                    TextStyle().apply {
                        size = 10f
                        placement = TextStyle.Placement.BOTTOM
                        offset = 5f
                    })
            }
        }
    }

    private fun getCurrentLocation() = Point(args.lat.toDouble(), args.lon.toDouble())


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


    override fun getViewModel() = MapViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMapBinding {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding
    }

    override fun getFragmentRepository(): LocationRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(LocationApi::class.java, token)
        val db = CoffeeHouseDatabase.invoke(requireContext())
        return LocationRepository(db, api)
    }

    companion object {
        private val POINT = Point(55.751280, 37.629720)
        private val POSITION = CameraPosition(POINT, 17.0f, 150.0f, 30.0f)

    }
}


