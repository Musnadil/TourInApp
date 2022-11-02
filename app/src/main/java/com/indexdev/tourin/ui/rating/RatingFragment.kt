package com.indexdev.tourin.ui.rating

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentRatingBinding
import com.indexdev.tourin.ui.maps.MapsFragment.Companion.NOTIF_ID
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment
import com.indexdev.tourin.ui.splashscreen.SplashScreenFragment.Companion.DEFAULT_VALUE

class RatingFragment : Fragment() {
    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!

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
//            get rating binding.ratingBar.rating
            manager.cancel(NOTIF_ID)
            ratingEdit.putString(SplashScreenFragment.IMG_URL, DEFAULT_VALUE)
            ratingEdit.putString(SplashScreenFragment.TOUR_NAME, DEFAULT_VALUE)
            ratingEdit.putString(SplashScreenFragment.ID_TOUR, DEFAULT_VALUE)
            ratingEdit.apply()
            findNavController().navigate(R.id.action_ratingFragment_to_homeFragment)
        }
        binding.tv.text = "Give a rating for $tourName"
        binding.tvTourName.text = "$tourName"
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}