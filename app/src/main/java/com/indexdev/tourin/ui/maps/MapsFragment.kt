package com.indexdev.tourin.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkBuilder
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
import com.indexdev.tourin.MainActivity
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.request.AddFacilityRateRequest
import com.indexdev.tourin.data.model.response.ResponsePOI
import com.indexdev.tourin.data.model.response.ResponseUserMitra
import com.indexdev.tourin.databinding.FragmentMapsBinding
import com.indexdev.tourin.services.LocationService
import com.indexdev.tourin.services.LocationService.Companion.DISTANCE
import com.indexdev.tourin.services.LocationService.Companion.FACILITY_NAME
import com.indexdev.tourin.services.LocationService.Companion.ID_MARKER
import com.indexdev.tourin.services.LocationService.Companion.TOUR_NAME_FACILITY_RATE
import com.indexdev.tourin.services.LocationService.Companion.UPDATE_DISTANCE
import com.indexdev.tourin.ui.*
import com.indexdev.tourin.ui.choosevehicle.ChooseVehicleFragment
import com.indexdev.tourin.ui.home.HomeFragment.Companion.ADDRESS
import com.indexdev.tourin.ui.home.HomeFragment.Companion.ID_TOUR
import com.indexdev.tourin.ui.home.HomeFragment.Companion.IMG_URL
import com.indexdev.tourin.ui.home.HomeFragment.Companion.LAT
import com.indexdev.tourin.ui.home.HomeFragment.Companion.LONG
import com.indexdev.tourin.ui.home.HomeFragment.Companion.TOUR_NAME
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private var mLocationService: LocationService = LocationService()
    private lateinit var mServiceIntent: Intent
    private val mapsViewModel: MapsViewModel by viewModels()
    private var idUser: String? = ""

    private lateinit var iconWorship: Bitmap
    private lateinit var iconToilet: Bitmap
    private lateinit var iconFoodPlace: Bitmap
    private lateinit var iconEvacuation: Bitmap
    private lateinit var iconParking: Bitmap
    private lateinit var iconMosque: Bitmap
    private lateinit var iconChurches: Bitmap
    private lateinit var iconTemples: Bitmap
    private lateinit var iconMonasteries: Bitmap
    private lateinit var iconKlenteng: Bitmap
    private lateinit var iconHomeStay: Bitmap
    private lateinit var iconRestaurant: Bitmap
    private lateinit var iconRentVehicle: Bitmap


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.uiSettings.isCompassEnabled = true
        val customInfoWindow = CustomInfoWindow(requireActivity())
        mMap.setInfoWindowAdapter(customInfoWindow)
        permissionLocation()
        getPOI()
        getPOIUser()
        mMap.isMyLocationEnabled = true
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
        iconWorship =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_poi_worship_place)
        iconToilet =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__toilet)
        iconFoodPlace =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__food_court)
        iconEvacuation =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__evacuation)
        iconParking =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__parking)
        iconMosque =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__masjid)
        iconChurches =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__gereja)
        iconTemples =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__pura)
        iconMonasteries =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__vihara)
        iconKlenteng =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_marker__klenteng)
        iconHomeStay =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_partnerin_hotel)
        iconRestaurant =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_partnerin_restaurant)
        iconRentVehicle =
            getBitmapFromVectorDrawable(requireActivity(), R.drawable.ic_partnerin_vehicle)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        mLocationService = LocationService()
        mServiceIntent = Intent(requireContext(), mLocationService.javaClass)

        setupFab()
        createNotificationChannel()

        latLngTour = LatLng(
            arguments?.getString(LAT).toString().toDouble(),
            arguments?.getString(LONG).toString().toDouble()
        )

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

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val lat = arguments?.getString(LAT)
        val long = arguments?.getString(LONG)
        binding.btnRoute.setOnClickListener {
            val googleMapsUrl =
                "https://maps.google.com/maps?daddr=${lat.toString().toDouble()},${
                    long.toString().toDouble()
                }"
            val uri = Uri.parse(googleMapsUrl)
            val googleMapsPackage = "com.google.android.apps.maps"
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage(googleMapsPackage)
            }
            startActivity(intent)
        }
        val preference = requireContext().getSharedPreferences(
            SplashScreenFragment.SHARED_PREF,
            Context.MODE_PRIVATE
        )
        idUser = preference.getString(
            SplashScreenFragment.ID_USER,
            SplashScreenFragment.DEFAULT_VALUE
        )
        requireActivity().registerReceiver(updateDistance, IntentFilter(DISTANCE))

    }

    private val updateDistance: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val distance = intent.getDoubleExtra(UPDATE_DISTANCE, 0.0)
            val idMarker = intent.getStringExtra(ID_MARKER)
            val facilityName = intent.getStringExtra(FACILITY_NAME)
            val tourName = intent.getStringExtra(TOUR_NAME_FACILITY_RATE)
            val notifManager = NotificationManagerCompat.from(requireContext())
            Log.d("distance_location",distance.toString())

            val pendingIntentFacility = NavDeepLinkBuilder(requireContext())
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.main_navigation)
                .setDestination(R.id.listRateFacilityFragment)
                .createPendingIntent()

            val notifFacility = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentTitle("Telah menggunakan fasilitas ${facilityName}?")
                .setContentText("Anda bisa beri rating untuk $facilityName")
                .setSmallIcon(R.drawable.logo_tourin)
                .setContentIntent(pendingIntentFacility)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            if (idMarker != null && idMarker !in listNotif) {
                listNotif.add(idMarker.toString())
                notifManager.notify(idMarker.toInt(), notifFacility)
                val request = AddFacilityRateRequest(
                    idUser.toString(),
                    idMarker,
                    facilityName.toString(),
                    tourName.toString(),
                    null
                )
                mapsViewModel.addFacilityRate(request)
            }
            Log.d("idpopo", listNotif.toString())

            val pendingIntent = NavDeepLinkBuilder(requireContext())
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.main_navigation)
                .setDestination(R.id.ratingFragment)
                .createPendingIntent()

            val preference = requireContext().getSharedPreferences(
                SplashScreenFragment.SHARED_PREF,
                Context.MODE_PRIVATE
            )
            val tourNameRate = preference.getString(
                SplashScreenFragment.TOUR_NAME,
                SplashScreenFragment.DEFAULT_VALUE
            )

            val notif = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentTitle("Telah berkunjung ke $tourNameRate?")
                .setContentText("Anda bisa beri rating untuk wisata $tourNameRate")
                .setSmallIcon(R.drawable.logo_tourin)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()


            if (distance <= 2 && arguments?.getString(ID_TOUR) !in listNotif) {
                inArea = true
            }
            if (inArea && distance >= 10) {
                inArea = false

                val idTour = preference.getString(
                    SplashScreenFragment.ID_TOUR,
                    SplashScreenFragment.DEFAULT_VALUE
                )
//                val ratingEdit = preference.edit()
//                ratingEdit.putString(
//                    SplashScreenFragment.ID_TOUR,
//                    idTour
//                )
//                ratingEdit.putString(
//                    SplashScreenFragment.IMG_URL,
//                    imgUrl
//                )
//                ratingEdit.putString(
//                    SplashScreenFragment.TOUR_NAME,
//                    tourName
//                )
//                ratingEdit.apply()
//                stopServiceFunc()
                distanceLocation = 0.0
                notifManager.notify(NOTIF_ID, notif)
                listNotif.add(idTour.toString())
            }
        }

    }

    // create notification
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager =
                requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun setupFab() {
        binding.apply {
            fabMyLocation.setOnClickListener {
                fabMenu.close(true)
                if (locationList.isNotEmpty()) {
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
            when (arguments?.getString(ID_TOUR)?.toInt()) {
                101 -> {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(touristSites, 10f))
                }
                102, 103, 106, 107, 109, 110 -> {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(touristSites, 15f))
                }
                104, 108 -> {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(touristSites, 14f))
                }
                105 -> {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(touristSites, 16f))
                }

            }
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
//        mMap.addMarker(markerOptions)
        val marker = mMap.addMarker(markerOptions)
        marker?.tag = "i"
    }

    private fun getPOI() {
        val tourId = arguments?.getString(ID_TOUR)?.toInt()
        mapsViewModel.getPoiList(tourId!!)
        mapsViewModel.poiList.observe(viewLifecycleOwner) { poiList ->
            when (poiList.status) {
                SUCCESS -> {
                    if (!poiList.data.isNullOrEmpty()) {
                        listPoi.addAll(poiList.data)
                        poiFacility(poiList.data)
                        for (i in poiList.data){
                            listPoiName.add(i.namaFasilitas)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Marker masih kosong", Toast.LENGTH_SHORT)
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
                "F06" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconMosque))
                }
                "F07" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconChurches))
                }
                "F08" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconTemples))
                }
                "F09" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconMonasteries))
                }
                "F10" -> {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconKlenteng))
                }
            }
            val marker = mMap.addMarker(markerOptions)
            marker?.tag = i
        }
    }

    private fun getPOIUser() {
        mapsViewModel.getPoiListMitra()
        mapsViewModel.poiListMitra.observe(viewLifecycleOwner) { poiListMitra ->
            when (poiListMitra.status) {
                SUCCESS -> {
                    if (!poiListMitra.data.isNullOrEmpty()) {
                        poiMitra(poiListMitra.data)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Marker mitra masih kosong",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                ERROR -> {
                    Toast.makeText(requireContext(), poiListMitra.message, Toast.LENGTH_SHORT)
                        .show()
                }
                LOADING -> {}
            }
        }

    }

    private fun poiMitra(facility: List<ResponseUserMitra>) {
        for (i in facility) {
            val latLong = LatLng(i.lat.toDouble(), i.longi.toDouble())
            if (i.kodeWisata == arguments?.getString(ID_TOUR) && i.status == "active" && !i.alamat.isNullOrEmpty() && !i.hariBuka.isNullOrEmpty()) {
                val markerOptions = MarkerOptions().title(i.idMitra).position(latLong)
                when (i.jenisUsaha) {
                    "Penginapan" -> {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconHomeStay))
                    }
                    "Rental Kendaraan" -> {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconRentVehicle))
                    }
                    "Rumah Makan" -> {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconRestaurant))
                    }
                }
                val marker = mMap.addMarker(markerOptions)
                marker?.tag = i
            }
        }
    }


    override fun onResume() {
        super.onResume()
        requestDevicesLocationSettings()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val facility = listPoiName
        mMap.setOnInfoWindowClickListener {
            if (marker.title in facility) {
                val dialogFragment = ChooseVehicleFragment(marker.position, marker.title.toString())
                activity?.let { dialogFragment.show(it.supportFragmentManager, null) }
            } else {
                val bundle = Bundle()
                bundle.putString(PARTNER_ID, marker.title)
                findNavController().navigate(
                    R.id.action_mapsFragment_to_productPartnerFragment,
                    bundle
                )
            }
        }
        return false
    }


    @SuppressLint("MissingPermission")
    fun permissionLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {

                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Izinkan aplikasi berjalan pada latar belakang")
                        setMessage("Aktifkan akses latar belakang lokasi untuk mendapatkan pengalaman yang lebih baik dengan aplikasi TourIn")
                        setPositiveButton("Izinkan") { _, _ ->
                            requestBackgroundLocationPermission()
                        }
                    }.create().show()

                } else if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    starServiceFunc()
                }
            } else {
                starServiceFunc()
            }

        } else if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Izin akses")
                    .setMessage("Izin lokasi diperlukan")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        requestFineLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                requestFineLocationPermission()
            }
        }
    }

    private fun starServiceFunc() {
        if (!Util.isMyServiceRunning(mLocationService.javaClass, requireActivity())) {
            context?.startService(mServiceIntent)
//            Toast.makeText(requireContext(), getString(R.string.service_start_successfully), Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), getString(R.string.service_already_running), Toast.LENGTH_SHORT).show()
        }

    }

    private fun stopServiceFunc() {
        context?.stopService(mServiceIntent)
        mLocationService.stopSelf()
//        if (Util.isMyServiceRunning(mLocationService.javaClass, requireActivity())) {

//            Toast.makeText(requireContext(), "Service stopped!!", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "Service is already stopped!!", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                MY_BACKGROUND_LOCATION_REQUEST
            )
        }
        starServiceFunc()
    }

    @SuppressLint("MissingPermission")
    private fun requestFineLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_FINE_LOCATION_REQUEST
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(requireContext(), requestCode.toString(), Toast.LENGTH_LONG).show()
        when (requestCode) {
            MY_FINE_LOCATION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        requestBackgroundLocationPermission()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "ACCESS_FINE_LOCATION permission denied",
                        Toast.LENGTH_LONG
                    ).show()
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", requireActivity().packageName, null)
                            )
                        )
                    }
                }
                return
            }
            MY_BACKGROUND_LOCATION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "Background location Permission Granted",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Background location permission denied",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    companion object {
        private const val MY_FINE_LOCATION_REQUEST = 99
        private const val MY_BACKGROUND_LOCATION_REQUEST = 100
        const val CHANNEL_ID = "channelID"
        const val CHANNEL_NAME = "channelName"
        const val NOTIF_ID = 0
        const val PARTNER_ID = "PARTNER_ID"

    }
}