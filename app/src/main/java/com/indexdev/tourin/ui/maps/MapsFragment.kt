package com.indexdev.tourin.ui.maps

import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.response.ResponsePOI
import com.indexdev.tourin.databinding.FragmentMapsBinding
import com.indexdev.tourin.ui.calculateDistanceInKM
import com.indexdev.tourin.ui.choosevehicle.ChooseVehicleFragment
import com.indexdev.tourin.ui.getBitmapFromVectorDrawable
import com.indexdev.tourin.ui.home.HomeFragment.Companion.ADDRESS
import com.indexdev.tourin.ui.home.HomeFragment.Companion.ID_TOUR
import com.indexdev.tourin.ui.home.HomeFragment.Companion.LAT
import com.indexdev.tourin.ui.home.HomeFragment.Companion.LONG
import com.indexdev.tourin.ui.home.HomeFragment.Companion.TOUR_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    var locationList: MutableList<Location> = ArrayList()
    private val mapsViewModel: MapsViewModel by viewModels()

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 3000
        fastestInterval = 3000
        priority = Priority.PRIORITY_HIGH_ACCURACY
        maxWaitTime = 5000
    }

    //last update location and calculate distance
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                val calculateDistance = calculateDistanceInKM(
                    location.latitude,
                    location.longitude,
                    arguments?.getString(LAT).toString().toDouble(),
                    arguments?.getString(LONG).toString().toDouble()
                )
                if (calculateDistance >= 0.008) {
//                    calculateDistance
                    // far from destination
                } else {
                    // in range of destination
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.uiSettings.isCompassEnabled = true
        mMap.isMyLocationEnabled = true
        val customInfoWindow = CustomInfoWindow(requireActivity())
        mMap.setInfoWindowAdapter(customInfoWindow)
        getLocationUpdate()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        setupFab()
        getPOI()

        //set tour name
        binding.tvTourName.text = arguments?.getString(TOUR_NAME)
        binding.tvAddress.text = arguments?.getString(ADDRESS)

        //get navBar Height
        val navBarHeight = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (navBarHeight > 0) {
            binding.navigationBar.layoutParams.height =
                resources.getDimensionPixelSize(navBarHeight)
        }

        //get statusBar Height
        val statusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (statusBarHeight > 0) {
            binding.statusBar.layoutParams.height = resources.getDimensionPixelSize(statusBarHeight)
        }

        // move button compass to top-right
        val compassButton: View = mapFragment!!.requireView().findViewWithTag("GoogleMapCompass")
        val rlp = compassButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_END)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, 0)
        rlp.topMargin = resources.getDimensionPixelSize(statusBarHeight)
        rlp.rightMargin = 16

        //activate fused location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val lat = arguments?.getString(LAT)
        val long = arguments?.getString(LONG)
        val touristSites = LatLng(lat.toString().toDouble(), long.toString().toDouble())
        binding.btnRoute.setOnClickListener {
            val dialogFragment =
                ChooseVehicleFragment(touristSites, arguments?.getString(TOUR_NAME).toString())
            activity?.let { dialogFragment.show(it.supportFragmentManager, null) }
        }
    }

    private fun setupFab() {
        binding.apply {
            fabMyLocation.setOnClickListener {
                fabMenu.close(true)
                if (!locationList.isNullOrEmpty()){
                    val location = locationList.last()
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13f))
                }
            }
            fabTourLocation.setOnClickListener {
                fabMenu.close(true)
                requestDevicesLocationSettings()
            }
        }
    }


    // ask for turn on gps
    private fun requestDevicesLocationSettings() {
        val locationReq = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationReq)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            //move camera to destination
            val lat = arguments?.getString(LAT)
            val long = arguments?.getString(LONG)
            val touristSites = LatLng(lat.toString().toDouble(), long.toString().toDouble())
            markerTour(touristSites)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(touristSites, 12f))
        }
        // always request to turn on gps
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        requireActivity(),
                        100
                    )
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }

    private fun markerTour(latLong: LatLng) {
        val markerOptions = MarkerOptions().position(latLong)
        mMap.addMarker(markerOptions)
    }

    private fun getPOI() {
        val tourId = arguments?.getString(ID_TOUR)?.toInt()
        mapsViewModel.getPoiList(tourId!!)
        mapsViewModel.poiList.observe(viewLifecycleOwner) { poiList ->
            when (poiList.status) {
                SUCCESS -> {
                    if (!poiList.data.isNullOrEmpty()) {
                        poiFacility(poiList.data)
                    } else {
                        Toast.makeText(requireContext(), "Can't get POI List", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                ERROR -> {
                    Toast.makeText(requireContext(), poiList.message, Toast.LENGTH_SHORT).show()
                }
                LOADING -> {}
            }
        }

    }

    private fun poiFacility(facility: List<ResponsePOI>) {
        for (i in facility) {
            val latLong = LatLng(i.lat.toDouble(), i.longi.toDouble())
            val iconWorship =
                getBitmapFromVectorDrawable(requireActivity(), R.drawable.worship_place_poi)
            val iconToilet = getBitmapFromVectorDrawable(requireActivity(), R.drawable.toilet_poi)
            val iconFoodPlace =
                getBitmapFromVectorDrawable(requireActivity(), R.drawable.food_place_poi)
            val iconEvacuation =
                getBitmapFromVectorDrawable(requireActivity(), R.drawable.evacuation_place_poi)
            val iconParking = getBitmapFromVectorDrawable(requireActivity(), R.drawable.parking_poi)
            val markerOptions = MarkerOptions().title(i.namaFasilitas).position(latLong)
            when (i.kodeFasilitas) {
                "F01" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconWorship))
                }
                "F02" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconToilet))
                }
                "F03" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFoodPlace))
                }
                "F04" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconEvacuation))
                }
                "F05" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconParking))
                }
            }
            val marker = mMap.addMarker(markerOptions)
            marker?.tag = i
        }


    }

    override fun onResume() {
        super.onResume()
        requestDevicesLocationSettings()
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        mMap.setOnInfoWindowClickListener {
//            val googleMapsUrl = "https://www.google.com/maps?q=${marker.position.latitude},${marker.position.longitude}"
            val dialogFragment = ChooseVehicleFragment(marker.position, marker.title.toString())
            activity?.let { dialogFragment.show(it.supportFragmentManager, null) }
        }
        return false
    }


    @SuppressLint("MissingPermission")
    private fun getLocationUpdate() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}