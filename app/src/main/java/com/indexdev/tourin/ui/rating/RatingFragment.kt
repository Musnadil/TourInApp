package com.indexdev.tourin.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.indexdev.tourin.R
import com.indexdev.tourin.databinding.FragmentRatingBinding
import com.indexdev.tourin.ui.home.HomeFragment
import com.indexdev.tourin.ui.home.HomeFragment.Companion.TOUR_NAME
import com.indexdev.tourin.ui.initiateNotify
import com.indexdev.tourin.ui.maps.MapsFragment.Companion.NOTIF_ID

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
        manager.cancel(NOTIF_ID)
        Glide.with(binding.root)
            .load(arguments?.getString(HomeFragment.IMG_URL))
            .transform(CenterCrop())
            .into(binding.ivTour)

        binding.btnSend.setOnClickListener {
            initiateNotify = false
            findNavController().navigate(R.id.action_ratingFragment_to_homeFragment)
        }
        binding.tv.text = "Give a rating for ${arguments?.getString(TOUR_NAME)}"
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}