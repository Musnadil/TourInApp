package com.indexdev.tourin.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentMapsBinding


class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMarkerClickListener(this)
        googleMap.uiSettings.isCompassEnabled = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

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
    }

    override fun onMarkerClick(p0: Marker) = false
}