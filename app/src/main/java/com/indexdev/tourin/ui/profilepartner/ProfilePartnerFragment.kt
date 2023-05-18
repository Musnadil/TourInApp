package com.indexdev.tourin.ui.profilepartner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentProfilePartnerBinding
import com.indexdev.tourin.ui.productpartner.ProductPartnerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilePartnerFragment : Fragment() {
    private var _binding: FragmentProfilePartnerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePartnerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (statusBarHeight > 0) {
            binding.statusbar.layoutParams.height = resources.getDimensionPixelSize(statusBarHeight)
        }
        val navigationBarHeight =
            resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (navigationBarHeight > 0) {
            binding.bottomNavBar.layoutParams.height =
                resources.getDimensionPixelSize(navigationBarHeight)
        }

        val imageUrl = arguments?.getString(ProductPartnerFragment.IMAGE_STORE)
        val businessName = arguments?.getString(ProductPartnerFragment.BUSINESS_NAME)
        val businessOwner = arguments?.getString(ProductPartnerFragment.BUSINESS_OWNER)
        val numberPhone = arguments?.getString(ProductPartnerFragment.NUMBER_PHONE)
        val address = arguments?.getString(ProductPartnerFragment.ADDRESS)
        val lat = arguments?.getString(ProductPartnerFragment.LAT)
        val longi = arguments?.getString(ProductPartnerFragment.LONG)
        val openingHours = arguments?.getString(ProductPartnerFragment.OPENING_HOURS)
        val closingHours = arguments?.getString(ProductPartnerFragment.CLOSING_HOURS)

        Glide.with(requireContext())
            .load("https://tourin.musnadil.my.id/gambar/$imageUrl")
            .transform(CenterCrop())
            .into(binding.ivBusiness)

        binding.tvBusinessNameTop.text = businessName
        binding.tvBusinessName.text = businessName
        binding.tvBusinessOwner.text = businessOwner
        binding.tvNumberPhone.text = numberPhone
        binding.tvAddress.text = address
        binding.tvOpengHours.text = "$openingHours - $closingHours"

        binding.btnVisit.setOnClickListener {
            val googleMapsUrl =
                "https://www.google.com/maps?q=${lat.toString().toDouble()},${
                    longi.toString().toDouble()
                }"
            val uri = Uri.parse(googleMapsUrl)
            val googleMapsPackage = "com.google.android.apps.maps"
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage(googleMapsPackage)
            }
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}