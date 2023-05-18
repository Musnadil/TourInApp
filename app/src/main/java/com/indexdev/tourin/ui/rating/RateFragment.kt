package com.indexdev.tourin.ui.rating

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.snackbar.Snackbar
import com.indexdev.tourin.R
import com.indexdev.tourin.data.api.Status
import com.indexdev.tourin.data.api.Status.*
import com.indexdev.tourin.data.model.request.RateRequest
import com.indexdev.tourin.databinding.FragmentRatingBinding
import com.indexdev.tourin.ui.maps.MapsFragment.Companion.NOTIF_ID
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.DEFAULT_VALUE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateFragment : Fragment() {
    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!
    private val rateViewModel: RateViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = NotificationManagerCompat.from(requireContext())
        val preference = requireContext().getSharedPreferences(
            SplashScreenFragment.SHARED_PREF,
            Context.MODE_PRIVATE)
        val ratingEdit = preference.edit()
        val imgUrl = preference.getString(SplashScreenFragment.IMG_URL,DEFAULT_VALUE)
        val tourName = preference.getString(SplashScreenFragment.TOUR_NAME,DEFAULT_VALUE)
        val tourID = preference.getString(SplashScreenFragment.ID_TOUR,DEFAULT_VALUE)
        Glide.with(binding.root)
            .load(imgUrl)
            .transform(CenterCrop())
            .into(binding.ivTour)

        binding.btnSend.setOnClickListener {
            val preference = requireContext().getSharedPreferences(SplashScreenFragment.SHARED_PREF,Context.MODE_PRIVATE)
            val idUser = preference.getString(SplashScreenFragment.ID_USER, DEFAULT_VALUE)
            if (idUser != DEFAULT_VALUE){
                val rateRequest = RateRequest(
                    null,
                    idUser.toString(),
                    tourID.toString(),
                    binding.ratingBar.rating.toString())
                rateViewModel.postRate(rateRequest)
            }
        }
        rateViewModel.rate.observe(viewLifecycleOwner){ resources ->
            when(resources.status){
                SUCCESS ->{
                    binding.btnSend.isEnabled = false
                    val snackbar = Snackbar.make(
                        binding.root, "Thank you for rating",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackbar.setAction("Oke"){
                        snackbar.dismiss()
                        manager.cancel(NOTIF_ID)
                        ratingEdit.putString(SplashScreenFragment.IMG_URL, DEFAULT_VALUE)
                        ratingEdit.putString(SplashScreenFragment.TOUR_NAME, DEFAULT_VALUE)
                        ratingEdit.putString(SplashScreenFragment.ID_TOUR, DEFAULT_VALUE)
                        ratingEdit.apply()
                        findNavController().navigate(R.id.action_ratingFragment_to_homeFragment)
                    }
                    snackbar.show()
                }
                ERROR -> {
                    binding.btnSend.isEnabled = false
                    val snackbar = Snackbar.make(
                        binding.root, "an error occurred",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackbar.setAction("Oke"){
                        manager.cancel(NOTIF_ID)
                        ratingEdit.putString(SplashScreenFragment.IMG_URL, DEFAULT_VALUE)
                        ratingEdit.putString(SplashScreenFragment.TOUR_NAME, DEFAULT_VALUE)
                        ratingEdit.putString(SplashScreenFragment.ID_TOUR, DEFAULT_VALUE)
                        ratingEdit.apply()
                        snackbar.dismiss()
                    }
                    snackbar.show()
                }
                LOADING -> {}
            }
        }
        binding.tv.text = "Sudah mengunjungi $tourName? Silakan beri nilai untuk $tourName"
        binding.tvTourName.text = "$tourName"
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}