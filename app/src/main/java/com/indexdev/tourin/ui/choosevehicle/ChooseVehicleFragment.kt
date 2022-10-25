package com.indexdev.tourin.ui.choosevehicle

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.model.LatLng
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentChooseVehicleBinding


class ChooseVehicleFragment() : DialogFragment() {
    private var _binding: FragmentChooseVehicleBinding? = null
    private val binding get() = _binding!!
    private lateinit var latLong: LatLng
    private lateinit var destination: String

    constructor(latLong: LatLng, destination: String) : this() {
        this.latLong = latLong
        this.destination = destination
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_dialog)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        _binding = FragmentChooseVehicleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var latitude = 0.0
        var longitude = 0.0
        var dest = ""
        if (this::latLong.isInitialized && this::destination.isInitialized) {
            latitude = latLong.latitude
            longitude = latLong.longitude
            dest = destination
        }
        binding.tvSelectVehicle.text = "Choose a vehicle to go to ${dest}"
        binding.btnCar.setOnClickListener {
            dialog?.dismiss()
            navigateGoogleMaps(latitude, longitude, "d")
        }
        binding.btnTwoWheeler.setOnClickListener {
            dialog?.dismiss()
            navigateGoogleMaps(latitude, longitude, "l")
        }
        binding.btnWalking.setOnClickListener {
            dialog?.dismiss()
            navigateGoogleMaps(latitude, longitude, "w")
        }
        binding.btnBicycle.setOnClickListener {
            dialog?.dismiss()
            navigateGoogleMaps(latitude, longitude, "b")
        }
    }

    private fun navigateGoogleMaps(latitude: Double, longitude: Double, mode: String) {
        val googleMapsUrl =
            "google.navigation:q=${latitude},${longitude}&mode=$mode"
        val uri = Uri.parse(googleMapsUrl)
        val googleMapsPackage = "com.google.android.apps.maps"
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage(googleMapsPackage)
        }
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}