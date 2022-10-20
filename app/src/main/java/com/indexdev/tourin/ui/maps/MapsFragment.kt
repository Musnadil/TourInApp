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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentMapsBinding
import com.indexdev.tourin.ui.home.HomeFragment.Companion.LAT
import com.indexdev.tourin.ui.home.HomeFragment.Companion.LONG
import com.indexdev.tourin.ui.home.HomeFragment.Companion.TOUR_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.acos
import kotlin.math.sin

@AndroidEntryPoint
class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 3000
        fastestInterval = 3000
        priority = Priority.PRIORITY_HIGH_ACCURACY
        maxWaitTime = 5000
    }

    //last update location and calculate distance
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                val calculateDistance = calculateDistanceInKM(
                    location.latitude,
                    location.longitude,
                    arguments?.getString(LAT).toString().toDouble(),
                    arguments?.getString(LONG).toString().toDouble())
                if (calculateDistance>= 0.008){
//                    calculateDistance
                    // far from destination
                }else{
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
        getLocationUpdate()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        //set tour name
        binding.tvTourName.text = arguments?.getString(TOUR_NAME)

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
    }

    private fun getPOI() {
        //get from api
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
        task.addOnSuccessListener { locationSettingsResponse ->
            //move camera to destination
            val lat = arguments?.getString(LAT)
            val long = arguments?.getString(LONG)
            val touristSites = LatLng(lat.toString().toDouble(), long.toString().toDouble())
            markerTour(touristSites)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(touristSites, 12f))
        }
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
        markerOptions.title(arguments?.getString(TOUR_NAME)?:"Destination Name")
        mMap.addMarker(markerOptions)
    }

    override fun onResume() {
        super.onResume()
        requestDevicesLocationSettings()
    }

    override fun onMarkerClick(p0: Marker) = false


    // calculate distance between 2 point
    private fun calculateDistanceInKM(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist =
            sin(deg2rad(lat1)) * sin(deg2rad(lat2)) +
                    kotlin.math.cos(deg2rad(lat1)) * kotlin.math.cos(deg2rad(lat2)) * kotlin.math.cos(
                deg2rad(theta)
            )
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344
        return dist

    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
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