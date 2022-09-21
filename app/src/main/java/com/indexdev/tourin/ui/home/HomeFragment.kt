package com.indexdev.tourin.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapsFragment)
        }
        greeting()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return

        }
    }
    @SuppressLint("SetTextI18n")
    private fun greeting(){
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 12..16 -> {binding.tvGreating.text = "Good Afternoon,"}
            in 17..20 -> {binding.tvGreating.text = "Good Evening,"}
            in 21..23 -> {binding.tvGreating.text = "Good Night,"}
            else -> {binding.tvGreating.text = "Good Morning,"}
        }
    }

    private fun locationPermission(){
    }
}